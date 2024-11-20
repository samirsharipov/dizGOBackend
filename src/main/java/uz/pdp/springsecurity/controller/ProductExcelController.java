package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.springsecurity.service.ProductExcelService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/product-excel")
@RequiredArgsConstructor
public class ProductExcelController {

    private final ProductExcelService productExcelService;

    @PostMapping("/upload/{branchId}")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file,
                                         @PathVariable UUID branchId) {
        try {
            CompletableFuture<Void> voidCompletableFuture = productExcelService.importFromExcelAsync(file, branchId);
            voidCompletableFuture.join();
            return ResponseEntity.ok("Mahsulotlar muvaffaqiyatli yuklandi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xatolik: " + e.getMessage());
        }
    }
}
