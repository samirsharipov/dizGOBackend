package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.repository.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    private final EmitterService emitterService;
    private final MeasurementTranslateRepository measurementTranslateRepository;

    private Map<String, Measurement> measurementCache;
    private Map<String, Category> categoryCache;
    private Map<String, Brand> brandCache;
    private Map<String, Language> languageCache;

    @Async("taskExecutor")
    public CompletableFuture<Void> importFromExcelAsync(MultipartFile file, UUID branchId, SseEmitter emitter) throws Exception {
        importFromExcel(file, branchId, emitter);
        return CompletableFuture.completedFuture(null);
    }


    public void importFromExcel(MultipartFile file, UUID branchId, SseEmitter emitter) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new Exception("Branch not found"));
            UUID businessId = branch.getBusiness().getId();

            // Keshni yuklash
            loadCaches(businessId);

            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int batchSize = Math.min(1000, Math.max(100, totalRows / 100)); // Maksimal 1000 ta batch
            List<Product> productsBatch = new ArrayList<>();
            List<ProductTranslate> translationsBatch = new ArrayList<>();
            int productsUploaded = 0;

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    List<Product> products = createProductsFromRow(row, branch, translationsBatch);
                    productsBatch.addAll(products);

                    if (productsBatch.size() >= batchSize) {
                        saveBatch(productsBatch);
                        saveBatchTranslations(translationsBatch);
                        productsUploaded += productsBatch.size();
                        productsBatch.clear();
                        translationsBatch.clear();
                    }

                    if (i % 100 == 0 || i == totalRows - 1) {
                        int progress = (i * 100) / totalRows;
//                        sendProgress(emitter, i, totalRows, productsUploaded);
                        emitterService.sendProgress(branchId, progress, productsUploaded);
                    }

                } catch (Exception e) {
                    emitterService.sendError(emitter, "Xatolik yuz berdi: " + e.getMessage());
                    emitter.completeWithError(e); // Xatolikni yakunlash
                }
            }

            if (!productsBatch.isEmpty()) {
                saveBatch(productsBatch);
                saveBatchTranslations(translationsBatch);
                productsUploaded += productsBatch.size();
                productsBatch.clear();
                translationsBatch.clear();
            }

            emitterService.sendCompletion(emitter, "Yuklash tugadi! Jami mahsulotlar: " + productsUploaded);
        }
    }

    private List<Product> createProductsFromRow(Row row, Branch branch, List<ProductTranslate> translationsBatch) {
        List<Product> products = new ArrayList<>();
        String barcodeCellValue = getCellValue(row, 1); // Barcode ustunini o'qish
        List<Branch> branches = new ArrayList<>();
        branches.add(branch);

        // Barcode ni vergul orqali ajratish
        String[] barcodes = barcodeCellValue.split(",");

        for (String barcode : barcodes) {
            try {
                Product product = productRepository
                        .findByBarcodeAndBusinessId(barcode, branch.getBusiness().getId())
                        .orElse(new Product());

                product.setName(getCellValue(row, 0));
                product.setBarcode(barcode); // Har bir barcode ni alohida tozalash va o'rnatish
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
                product.setBranch(branches);

                product.setBusiness(branch.getBusiness());

                setCategoryMeasurementBrand(product, row, branch);

                ProductTranslate productTranslate = saveTranslateIfNeeded(row, product);
                if (productTranslate != null) {
                    translationsBatch.add(productTranslate);
                }

                products.add(product);
            } catch (Exception e) {
                continue;
            }
        }
        return products;
    }

    private void setCategoryMeasurementBrand(Product product, Row row, Branch branch) {
        String measurementName = getCellValue(row, 3);
        String categoryName = getCellValue(row, 4);
        String brandName = getCellValue(row, 6);
        String languageCode = getCellValue(row, 2);

        if (!measurementName.isBlank()) {
            product.setMeasurement(findOrCreateMeasurement(measurementName, branch, languageCode));
        }
        if (!categoryName.isBlank()) {
            product.setCategory(findOrCreateCategory(categoryName, branch, languageCode));
        }
        if (!brandName.isBlank()) {
            product.setBrand(findOrCreateBrand(brandName, branch));
        }
    }

    private ProductTranslate saveTranslateIfNeeded(Row row, Product product) {
        String languageCode = getCellValue(row, 2);
        Language language = findLanguageByCode(languageCode);
        if (language != null) {
            ProductTranslate productTranslate = productTranslateRepository
                    .findByProductIdAndLanguage_Id(product.getId(), language.getId())
                    .orElse(new ProductTranslate());

            productTranslate.setLanguage(language);
            productTranslate.setName(getCellValue(row, 0));
            productTranslate.setDescription(getCellValue(row, 22));
            productTranslate.setLongDescription(getCellValue(row, 23));
            productTranslate.setProduct(product);
            return productTranslate;
        }
        return null;
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell == null ? "" : cell.toString().trim();
    }

    private Double parseDouble(String value) {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void loadCaches(UUID businessId) {
        measurementCache = measurementRepository.findByBusinessId(businessId)
                .stream().collect(Collectors.toMap(Measurement::getName, m -> m));
        categoryCache = categoryRepository.findByBusiness_Id(businessId)
                .stream().collect(Collectors.toMap(Category::getName, c -> c));
        brandCache = brandRepository.findByBusiness_Id(businessId)
                .stream().collect(Collectors.toMap(Brand::getName, b -> b));
        languageCache = languageRepository.findAll()
                .stream().collect(Collectors.toMap(Language::getCode, l -> l));
    }

    private Measurement findOrCreateMeasurement(String name, Branch branch, String languageCode) {
        return measurementCache.computeIfAbsent(name, n -> {
            Measurement newMeasurement = new Measurement(branch.getBusiness(), n, true, false);
            Measurement measurement = measurementRepository.save(newMeasurement);
            MeasurementTranslate measurementTranslate = new MeasurementTranslate(n, null, measurement, languageCache.get(languageCode));
            measurementTranslateRepository.save(measurementTranslate);
            return measurement;
        });
    }

    private Category findOrCreateCategory(String name, Branch branch, String languageCode) {
        return categoryCache.computeIfAbsent(name, n -> {
            Category newCategory = new Category(branch.getBusiness(), n, true, false);
            Category category = categoryRepository.save(newCategory);
            CategoryTranslate categoryTranslate = new CategoryTranslate(n, null, category, languageCache.get(languageCode));
            categoryTranslate.setCategory(category);
            return category;
        });
    }

    private Brand findOrCreateBrand(String name, Branch branch) {
        return brandCache.computeIfAbsent(name, n -> {
            Brand newBrand = new Brand(branch.getBusiness(), n, true, false);
            return brandRepository.save(newBrand);
        });
    }

    private Language findLanguageByCode(String code) {
        return languageCache.get(code);
    }

    private void saveBatch(List<Product> productsBatch) {
        // Mahsulotlarni tekshirib, yangi yoki yangilanadigan mahsulotlarni ajratish
        Set<String> existingBarcodes = productsBatch.stream()
                .map(product -> product.getBarcode() + ":" + product.getBusiness().getId()) // barcode va businessId kombinatsiyasi
                .collect(Collectors.toSet());

        List<Product> productsToSave = new ArrayList<>();

        for (Product product : productsBatch) {
            String barcodeBusinessIdKey = product.getBarcode() + ":" + product.getBusiness().getId();

            // Agar mahsulot yangi yoki yangilanadigan bo'lsa, uni qo'shish
            if (existingBarcodes.contains(barcodeBusinessIdKey)) {
                // Mavjud mahsulotni yangilash
                boolean existingProduct = productRepository
                        .existsByBarcodeAndBusinessId(product.getBarcode(), product.getBusiness().getId());

                if (!existingProduct) {
                    productsToSave.add(product);  // Yangilangan mahsulotni qo'shish
                }
                existingBarcodes.remove(barcodeBusinessIdKey);
            }
        }

        // Mahsulotlarni saqlash
        if (!productsToSave.isEmpty()) {
            productRepository.saveAll(productsToSave); // Yangi va yangilangan mahsulotlarni saqlash
        }
    }

    private void saveBatchTranslations(List<ProductTranslate> translationsBatch) {
        // Mahsulotning barcha tarjimalarini guruhlash
        Map<String, List<ProductTranslate>> groupedTranslations = translationsBatch.stream()
                .collect(Collectors.groupingBy(translation -> translation.getProduct().getBarcode() + ":" + translation.getProduct().getBusiness().getId()));

        List<ProductTranslate> translationsToSave = new ArrayList<>();

        for (Map.Entry<String, List<ProductTranslate>> entry : groupedTranslations.entrySet()) {
            String barcodeBusinessKey = entry.getKey();
            List<ProductTranslate> translationList = entry.getValue();

            // Ushbu barcode va businessId uchun mahsulotni topish
            String[] keys = barcodeBusinessKey.split(":");
            String barcode = keys[0];
            UUID businessId = UUID.fromString(keys[1]);

            Product product = productRepository.findByBarcodeAndBusinessId(barcode, businessId).orElse(null);

            if (product != null) {
                for (ProductTranslate translation : translationList) {
                    // Mevjud tarjimani tekshirish yoki yangi tarjima qo'shish
                    ProductTranslate existingTranslation = productTranslateRepository
                            .findByProductIdAndLanguage_Id(product.getId(), translation.getLanguage().getId())
                            .orElse(null);

                    if (existingTranslation != null) {
                        // Mavjud tarjimani yangilash
                        existingTranslation.setName(translation.getName());
                        existingTranslation.setDescription(translation.getDescription());
                        existingTranslation.setLongDescription(translation.getLongDescription());
                        translationsToSave.add(existingTranslation);
                    } else {
                        // Yangi tarjima qo'shish
                        translation.setProduct(product);
                        translationsToSave.add(translation);
                    }
                }
            }
        }

        // Tarjimalarni saqlash
        if (!translationsToSave.isEmpty()) {
            productTranslateRepository.saveAll(translationsToSave);
        }
    }
}