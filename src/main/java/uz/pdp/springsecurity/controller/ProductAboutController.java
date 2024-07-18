package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.ProductAboutService;
import uz.pdp.springsecurity.utils.AppConstant;

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
