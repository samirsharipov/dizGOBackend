package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.entity.SelectedProducts;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.SelectedProductsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/selected-products")
@RequiredArgsConstructor
public class SelectedProductsController {

    private final ResponseEntityHelper responseHelper;
    private final SelectedProductsService service;

    @PostMapping
    public HttpEntity<ApiResponse> createSelectedProducts(@RequestBody SelectedProducts selectedProducts) {
        return responseHelper.buildResponse(service.createSelectedProducts(selectedProducts));
    }

    @PutMapping("/{id}")
    public HttpEntity<ApiResponse> updateSelectedProducts(@PathVariable UUID id, @RequestBody SelectedProducts selectedProducts) {
        return responseHelper.buildResponse(service.updateSelectedProducts(id,selectedProducts));
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getSelectedProducts(@PathVariable UUID id,@RequestParam String languageCode) {
        return responseHelper.buildResponse(service.getSelectedProducts(id, languageCode));
    }

    @GetMapping("/get-by-branch-id/{branchId}")
    public HttpEntity<ApiResponse> getSelectedProductsByBranchId(@PathVariable UUID branchId, @RequestParam String languageCode) {
        return responseHelper.buildResponse(service.getSelectedProductsByBranchId(branchId,languageCode));
    }
}
