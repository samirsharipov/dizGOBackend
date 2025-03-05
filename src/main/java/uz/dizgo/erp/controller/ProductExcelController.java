package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import uz.dizgo.erp.service.EmitterService;
import uz.dizgo.erp.service.ProductExcelService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/product-excel")
@RequiredArgsConstructor
public class ProductExcelController {

    private final ProductExcelService productExcelService;
    private final EmitterService emitterService;

    @PostMapping("/upload/{branchId}")
    public SseEmitter uploadExcel(@RequestParam("file") MultipartFile file, @PathVariable UUID branchId) {
        SseEmitter emitter = emitterService.getOrCreateEmitter(branchId);
        CompletableFuture.runAsync(() -> {
            try {
                CompletableFuture<Void> voidCompletableFuture =
                        productExcelService.importFromExcelAsync(file, branchId, emitter);
                voidCompletableFuture.thenRun(emitter::complete);
            } catch (Exception e) {
                emitterService.sendError(emitter, "Xatolik yuz berdi: " + e.getMessage());
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @GetMapping(value = "/start/{branchId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getSavedData(@PathVariable UUID branchId) {
        return emitterService.getEmitter(branchId);
    }
}