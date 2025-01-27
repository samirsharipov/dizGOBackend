package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.enums.DiscountType;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.DiscountDto;
import uz.pdp.springsecurity.payload.DiscountEditDto;
import uz.pdp.springsecurity.service.DiscountService;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
    private final ResponseEntityHelper responseEntityHelper;

    // Chegirma yaratish
    @PostMapping
    public ResponseEntity<ApiResponse> createDiscount(@RequestBody DiscountDto discountDto) {
        return responseEntityHelper.buildResponse(discountService.createDiscount(discountDto));
    }

    // Chegirma olish
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDiscount(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(discountService.getDiscountById(id));
    }

    // Chegirma ro‘yxatini olish
    @GetMapping("/get-by-business-id/{businessId}")
    public ResponseEntity<ApiResponse> getAllDiscounts(@PathVariable UUID businessId, @RequestParam(required = false) UUID branchId, @RequestParam(required = false) DiscountType type,
                                                       @RequestParam(required = false) Timestamp startDate, @RequestParam(required = false) Timestamp endDate) {
        return responseEntityHelper.buildResponse(discountService.getAllDiscounts(businessId, branchId, type, startDate, endDate));
    }

    // Chegirmani yangilash
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDiscount(@PathVariable UUID id, @RequestBody DiscountEditDto discountDetails) {
        return responseEntityHelper.buildResponse(discountService.updateDiscount(id, discountDetails));
    }

    // Chegirmani faolsiz qilish
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateDiscount(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(discountService.deactivateDiscount(id));
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ApiResponse> active(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(discountService.active(id));
    }

    // Chegirmani o‘chirish
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<ApiResponse> forceDeleteDiscount(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(discountService.deleteDiscount(id));
    }

    @GetMapping("/search/{branch_id}")
    public HttpEntity<?> search(@PathVariable UUID branch_id,
                                @RequestParam String name,
                                @RequestParam String language) {
        return responseEntityHelper.buildResponse(discountService.search(branch_id, name, language));
    }

    @GetMapping("check-products")
    public HttpEntity<?> checkProducts(@RequestParam List<UUID> productIds) {
        return responseEntityHelper.buildResponse(discountService.checkProductList(productIds));
    }

}