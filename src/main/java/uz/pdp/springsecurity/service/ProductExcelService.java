package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.entity.Brand;
import uz.pdp.springsecurity.entity.Category;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.repository.BrandRepository;
import uz.pdp.springsecurity.repository.CategoryRepository;
import uz.pdp.springsecurity.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductExcelService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public void saveProductsFromExcel(MultipartFile file) throws Exception {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Headerdan keyingi qatorlar
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Product product = new Product();

            // Excel ustunlarini o'qib, mahsulot obyektini to'ldirish
            product.setName(getCellValue(row.getCell(0)));
            product.setBarcode(getCellValue(row.getCell(1)));
            product.setPluCode(getCellValue(row.getCell(2)));
            product.setDescription(getCellValue(row.getCell(3)));
            product.setAttributes(getCellValue(row.getCell(4)));

            // Categoriya va brendni bog'lash
            String categoryName = getCellValue(row.getCell(5));
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new IllegalArgumentException("Category topilmadi: " + categoryName));
            product.setCategory(category);

            String brandName = getCellValue(row.getCell(6));
            Brand brand = brandRepository.findByName(brandName)
                    .orElseThrow(() -> new IllegalArgumentException("Brand topilmadi: " + brandName));
            product.setBrand(brand);

            product.setHsCode12(getCellValue(row.getCell(7)));
            product.setUniqueSKU(getCellValue(row.getCell(8)));
            product.setLength(Double.parseDouble(getCellValue(row.getCell(9))));
            product.setWidth(Double.parseDouble(getCellValue(row.getCell(10))));
            product.setHeight(Double.parseDouble(getCellValue(row.getCell(11))));
            product.setWeight(Double.parseDouble(getCellValue(row.getCell(12))));

            product.setShippingClass(getCellValue(row.getCell(13)));
            product.setLangGroup(getCellValue(row.getCell(14)));

            productRepository.save(product); // Mahsulotni saqlash
        }
        workbook.close();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> null;
        };
    }
}
