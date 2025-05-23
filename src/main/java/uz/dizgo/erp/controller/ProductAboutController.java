package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.ProductAboutService;
import uz.dizgo.erp.utils.AppConstant;

import java.util.UUID;

@RestController
@RequestMapping("/api/productAbout")
@RequiredArgsConstructor
public class ProductAboutController {
    private final ProductAboutService productAboutService;

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/{productId}")
    public HttpEntity<?> getOne(@PathVariable UUID productId,
                                @RequestParam(required = false) UUID branchId) {
        ApiResponse apiResponse = productAboutService.getOne(productId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/amount/{productId}")
    public HttpEntity<?> getOneAmount(@PathVariable UUID productId,
                                      @RequestParam(required = false) UUID branchId,
                                      @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                      @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productAboutService.getOneAmount(productId, branchId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
