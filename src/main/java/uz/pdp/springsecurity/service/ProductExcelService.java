package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductExcelService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MeasurementRepository measurementRepository;
    private final BranchRepository branchRepository;
    private final LanguageRepository languageRepository;
    private final ProductTranslateRepository productTranslateRepository;

    @Async("taskExecutor")
    public CompletableFuture<Void> importFromExcelAsync(MultipartFile file, UUID branchId) throws Exception {
        importFromExcel(file, branchId);  // Asinxron metodni chaqirish
        return CompletableFuture.completedFuture(null);  // Asinxron jarayonni tugallash
    }

    public void importFromExcel(MultipartFile file, UUID branchId) throws Exception {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            throw new Exception("Branch not found");
        }

        Branch branch = optionalBranch.get();
        UUID businessId = branch.getBusiness().getId();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        int batchSize = 1000;
        List<Product> productsBatch = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                Product product = new Product();

                Optional<Measurement> optionalMeasurement = measurementRepository.findByBusinessIdAndName(businessId, getCellValue(row, 3));
                if (optionalMeasurement.isEmpty()) {
                    System.out.println("Measurement not found for row " + (i + 1));
                    continue;
                }
                Optional<Category> optionalCategory = categoryRepository.findByBusiness_IdAndName(businessId, getCellValue(row, 4));
                if (optionalCategory.isEmpty()) {
                    System.out.println("Category not found for row " + (i + 1));
                    continue;
                }

                Optional<Brand> optionalBrand = brandRepository.findByBusiness_IdAndName(businessId, getCellValue(row, 6));
                if (optionalBrand.isEmpty()) {
                    System.out.println("Brand not found for row " + (i + 1));
                    continue;
                }

                Measurement measurement = optionalMeasurement.get();
                Category category = optionalCategory.get();
                Brand brand = optionalBrand.get();

                product.setMeasurement(measurement); // O'lchov birligi
                product.setCategory(category);       // Kategoriya
                product.setBrand(brand);             // Brend

                product.setName(getCellValue(row, 0));
                product.setBarcode(getCellValue(row, 1));
                product.setLangGroup(getCellValue(row, 2));
                product.setPluCode(getCellValue(row, 5));
                product.setMXIKCode(getCellValue(row, 7));
                product.setAgreementExportsID(getCellValue(row, 8));
                product.setAgreementExportsPID(getCellValue(row, 9));
                product.setAgreementLocalID(getCellValue(row, 10));
                product.setAgreementLocalPID(getCellValue(row, 11));
                product.setHsCode12(getCellValue(row, 12));
                product.setHsCode22(getCellValue(row, 13));
                product.setHsCode32(getCellValue(row, 14));
                product.setHsCode44(getCellValue(row, 15));
                product.setUniqueSKU(getCellValue(row, 16));
                product.setLength(parseDouble(getCellValue(row, 17)));
                product.setWidth(parseDouble(getCellValue(row, 18)));
                product.setHeight(parseDouble(getCellValue(row, 19)));
                product.setWeight(parseDouble(getCellValue(row, 20)));
                product.setShippingClass(getCellValue(row, 21));
                product.setDescription(getCellValue(row, 22));
                product.setLongDescription(getCellValue(row, 23));
                product.setAttributes(getCellValue(row, 24));
                product.setKeywords(getCellValue(row, 25));
                product.setIsGlobal(true);
                product.setActive(true);
                product.setDeleted(false);

                // Tilni o'rnatish va ProductTranslate obyektini yaratish
                String languageCode = getCellValue(row, 2);  // Bu yerda til kodi olinadi (masalan, "ru", "en", "uz")
                Optional<Language> optionalLanguage = languageRepository.findByCode(languageCode);

                productsBatch.add(product);
                savedTranslate(optionalLanguage, row);

                if (productsBatch.size() >= batchSize) {
                    productRepository.saveAll(productsBatch);
                    productsBatch.clear();  // Partiyani tozalash
                }
            } catch (Exception e) {
                System.out.println("Error processing row " + (i + 1) + ": " + e.getMessage());
                continue;
            }
        }

        if (!productsBatch.isEmpty()) {
            productRepository.saveAll(productsBatch);
        }

        workbook.close();
    }

    private void savedTranslate(Optional<Language> optionalLanguage, Row row) {
        if (optionalLanguage.isPresent()) {
            Language language = optionalLanguage.get();
            ProductTranslate productTranslate = new ProductTranslate();
            productTranslate.setLanguage(language);  // Tilni o'rnatish
            productTranslate.setName(getCellValue(row, 0));  // Mahsulot nomi
            productTranslate.setDescription(getCellValue(row, 22));  // Ta'rif
            productTranslate.setLongDescription(getCellValue(row, 23));  // Uzun ta'rif

            productTranslateRepository.save(productTranslate);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell == null ? null : cell.toString();
    }

    private Double parseDouble(String value) {
        try {
            return value == null ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void importMultipleFiles(MultipartFile[] files, UUID branchId) throws Exception {
        CompletableFuture<Void>[] importTasks = new CompletableFuture[files.length];

        for (int i = 0; i < files.length; i++) {
            importTasks[i] = importFromExcelAsync(files[i], branchId)
                    .exceptionally(ex -> {
                        System.out.println("Error importing file: " + ex.getMessage());
                        return null;  // Xatolik bo'lsa, jarayonni davom ettirish
                    });
        }

        CompletableFuture.allOf(importTasks).join();
    }
}