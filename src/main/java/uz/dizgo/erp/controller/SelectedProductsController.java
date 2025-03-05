package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.entity.SelectedProducts;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.SelectedProductsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/selected-products")
@RequiredArgsConstructor
public class SelectedProductsController {

    private final ResponseEntityHelper responseHelper;
    private final SelectedProductsService service;

    @PostMapping("/create-list")
    public HttpEntity<ApiResponse> createSelectedProductsList(@RequestBody List<SelectedProducts> selectedProductsList) {
        return responseHelper.buildResponse(service.createSelectedProducts(selectedProductsList));
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getSelectedProducts(@PathVariable UUID id,@RequestParam String languageCode) {
        return responseHelper.buildResponse(service.getSelectedProducts(id, languageCode));
    }

    @GetMapping("/get-by-branch-id/{branchId}")
    public HttpEntity<ApiResponse> getSelectedProductsByBranchId(@PathVariable UUID branchId, @RequestParam String languageCode) {
        return responseHelper.buildResponse(service.getSelectedProductsByBranchId(branchId,languageCode));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteSelectedProducts(@PathVariable UUID id) {
        return responseHelper.buildResponse(service.delete(id));
    }
}
