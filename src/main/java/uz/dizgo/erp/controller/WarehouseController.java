package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.WarehouseService;
import uz.dizgo.erp.utils.AppConstant;

import java.util.UUID;

@RestController
@RequestMapping("api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping("/{businessId}")
    public HttpEntity<?> getLessProduct(@PathVariable UUID businessId,
                                        @RequestParam(required = false) UUID branchId,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {

        ApiResponse apiResponse = warehouseService.getLessProduct(businessId,branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/less-sold/{branchId}")
    public HttpEntity<?> getLessSoldProduct(@PathVariable UUID branchId,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {

        ApiResponse apiResponse = warehouseService.getLessSoldProduct(branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
