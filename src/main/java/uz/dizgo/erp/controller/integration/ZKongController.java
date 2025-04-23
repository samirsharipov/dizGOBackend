package uz.dizgo.erp.controller.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.integtation.ZKongAddProductDto;
import uz.dizgo.erp.service.integration.ZKongService;

@RestController
@RequestMapping("/api/zkong")
@RequiredArgsConstructor
public class ZKongController {

    private final ZKongService zKongService;

    @PostMapping("/update-price")
    public ResponseEntity<?> updatePrice(@RequestBody uz.dizgo.erp.payload.integtation.ZKongPriceUpdateRequest request) {
        boolean success = zKongService.updatePrice(request.getBarcode(),request.getPrice(),request.getStoreId());
        if (success) {
            return ResponseEntity.ok("Price updated on ZKONG");
        } else {
            return ResponseEntity.status(500).body("Failed to update price on ZKONG");
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestBody ZKongAddProductDto dto){
        boolean success = zKongService.bindProductToDevice(dto);
        if (success) {
            return ResponseEntity.ok("Price updated on ZKONG");
        } else {
            return ResponseEntity.status(500).body("Failed to update price on ZKONG");
        }
    }
}
