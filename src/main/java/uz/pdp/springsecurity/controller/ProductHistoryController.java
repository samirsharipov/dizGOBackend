package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.ProductHistoryService;
import uz.pdp.springsecurity.utils.AppConstant;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("api/productHistory")
@RequiredArgsConstructor
public class ProductHistoryController {
    private final ProductHistoryService productHistoryService;

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/{branchId}")
    public HttpEntity<?> get(@PathVariable UUID branchId,
                             @RequestParam Date date,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                             @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {

        ApiResponse apiResponse = productHistoryService.get(branchId, date, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("amount/{branchId}")
    public HttpEntity<?> amount(@PathVariable UUID branchId,
                                @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productHistoryService.amount(branchId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
