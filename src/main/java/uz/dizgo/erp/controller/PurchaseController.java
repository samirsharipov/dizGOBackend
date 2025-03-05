package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.PurchaseDto;
import uz.dizgo.erp.service.PurchaseService;
import uz.dizgo.erp.utils.AppConstant;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_PURCHASE")
    @PostMapping
    public HttpEntity<?> add(@RequestBody PurchaseDto purchaseDto) {
        ApiResponse apiResponse = purchaseService.add(purchaseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_PURCHASE")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PurchaseDto purchaseDto) {
        ApiResponse apiResponse = purchaseService.edit(id, purchaseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PURCHASE")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id) {
        ApiResponse apiResponse = purchaseService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PURCHASE")
    @GetMapping("/view/{purchaseId}")
    public HttpEntity<?> view(@PathVariable UUID purchaseId) {
        ApiResponse apiResponse = purchaseService.view(purchaseId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PURCHASE")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = purchaseService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PURCHASE_ADMIN")
    @GetMapping("/get-by-business/{businessId}")
    public HttpEntity<?> getByBusiness(@PathVariable UUID businessId, @RequestParam(required = false) UUID branchId,
                                       @RequestParam(required = false) UUID userId, @RequestParam(required = false) UUID supplierId,
                                       @RequestParam(required = false) Timestamp startDate, @RequestParam(required = false) Timestamp endDate,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                       @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        return responseEntityHelper.buildResponse(purchaseService.getByBusiness(businessId, branchId, userId, supplierId, startDate, endDate, status, page, size));
    }

    @GetMapping("/get-debt-purchase-by-supplierId/{supplierId}")
    public HttpEntity<?> getDebtPurchaseBySupplierId(@PathVariable UUID supplierId,
                                                     @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                                     @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        return responseEntityHelper.buildResponse(purchaseService.getDebtPurchaseBySupplierId(supplierId, page, size));
    }
}
