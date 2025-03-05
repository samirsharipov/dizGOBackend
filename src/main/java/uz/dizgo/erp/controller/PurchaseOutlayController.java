package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.PurchaseOutlayService;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase-outlay")
@RequiredArgsConstructor
public class PurchaseOutlayController {

    private final PurchaseOutlayService service;

    @GetMapping("/get-by-purchase-id/{purchaseId}")
    public ResponseEntity<?> getByPurchaseId(@PathVariable UUID purchaseId) {
        ApiResponse apiResponse = service.getByPurchaseId(purchaseId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
