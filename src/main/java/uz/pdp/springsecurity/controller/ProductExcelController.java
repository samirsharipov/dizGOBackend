package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.service.ProductExcelService;

@RestController
@RequestMapping("/product-excel")
@RequiredArgsConstructor
public class ProductExcelController {

    private final ProductExcelService productExcelService;

    @PostMapping("/upload-excel")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')") // Faqat Super Adminlar
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            productExcelService.saveProductsFromExcel(file);
            return ResponseEntity.ok("Mahsulotlar muvaffaqiyatli yuklandi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xatolik: " + e.getMessage());
        }
    }
}
