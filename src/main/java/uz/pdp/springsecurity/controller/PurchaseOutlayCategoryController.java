package uz.pdp.springsecurity.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.entity.PurchaseOutlayCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PurchaseOutlayCategoryDto;
import uz.pdp.springsecurity.service.PurchaseOutlayCategoryService;

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
