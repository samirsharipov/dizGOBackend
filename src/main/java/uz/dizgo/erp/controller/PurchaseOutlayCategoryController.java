package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.PurchaseOutlayCategoryDto;
import uz.dizgo.erp.service.PurchaseOutlayCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase-outlay-category")
@RequiredArgsConstructor
public class PurchaseOutlayCategoryController {

    private final PurchaseOutlayCategoryService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PurchaseOutlayCategoryDto purchaseOutlayCategoryDto) {
        ApiResponse apiResponse = service.create(purchaseOutlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@PathVariable UUID id, @RequestBody PurchaseOutlayCategoryDto purchaseOutlayCategoryDto) {
        ApiResponse apiResponse = service.edit(id,purchaseOutlayCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @GetMapping("/get-all-by-business-id/{businessId}")
    public ResponseEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

}
