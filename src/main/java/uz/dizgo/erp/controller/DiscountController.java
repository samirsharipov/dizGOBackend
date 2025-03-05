package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.enums.DiscountType;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.DiscountDto;
import uz.dizgo.erp.payload.DiscountEditDto;
import uz.dizgo.erp.service.DiscountService;

import javax.validation.Valid;
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
    public ResponseEntity<ApiResponse> createDiscount(@RequestBody @Valid DiscountDto discountDto) {
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
    public ResponseEntity<ApiResponse> search(@PathVariable UUID branch_id,
                                              @RequestParam String name,
                                              @RequestParam String language) {
        return responseEntityHelper.buildResponse(discountService.search(branch_id, name, language));
    }

    @GetMapping("check-products")
    public HttpEntity<ApiResponse> checkProducts(@RequestParam List<UUID> productIds,
                                                 @RequestParam List<UUID> branchIds) {
        return responseEntityHelper.buildResponse(discountService.checkProductList(productIds, branchIds));
    }

}