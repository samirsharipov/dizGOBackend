package uz.dizgo.erp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.*;
import uz.dizgo.erp.service.ProductService;
import uz.dizgo.erp.utils.AppConstant;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_PRODUCT")
    @PostMapping()
    public HttpEntity<?> add(@RequestBody ProductPostDto productPostDto) {
        ApiResponse apiResponse = productService.createProduct(productPostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("EDIT_PRODUCT")
    @PutMapping("edit-product/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ProductEditDto productDto) throws JsonProcessingException {
        ApiResponse apiResponse = productService.editProduct(id, productDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("EDIT_PRODUCT_MAIN")
    @PutMapping("edit-product-main/{id}")
    public HttpEntity<?> editProductMain(@PathVariable UUID id, @RequestBody ProductEditMainDto productEditMainDto) {
        ApiResponse apiResponse = productService.editProductMain(id, productEditMainDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.getProduct(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @CheckPermission("DELETE_PRODUCT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOne(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.deleteProduct(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @CheckPermission("DELETE_PRODUCT")
    @DeleteMapping("/delete-few")
    public HttpEntity<?> deleteFew(@RequestBody ProductIdsDto productIdsDto) {
        ApiResponse apiResponse = productService.deleteProducts(productIdsDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }


    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-barcode/{barcode}/{branchId}")
    public HttpEntity<?> getByBarcode(@PathVariable String barcode,
                                      @PathVariable UUID branchId) {
        return responseEntityHelper.buildResponse(productService.getByBarcode(barcode, branchId));
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/search/{branch_id}")
    public HttpEntity<?> search(@PathVariable UUID branch_id,
                                @RequestParam String name,
                                @RequestParam String language) {
        return responseEntityHelper.buildResponse(productService.search(branch_id, name, language));
    }


    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/search-for-trade/{branch_id}")
    public HttpEntity<?> searchTrade(@PathVariable UUID branch_id,
                                     @RequestParam String name,
                                     @RequestParam String language) {
        return responseEntityHelper.buildResponse(productService.searchTrade(branch_id, name, language));
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/search-barcode/{branch_id}")
    public HttpEntity<?> searchBarcode(@PathVariable UUID branch_id,
                                     @RequestParam String barcode) {
        return responseEntityHelper.buildResponse(productService.searchBarcode(branch_id, barcode));
    }


    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-category/{category_id}")
    public HttpEntity<?> getByCategory(@PathVariable UUID category_id,
                                       @RequestParam String code) {
        return responseEntityHelper.buildResponse(productService.getByCategory(category_id, code));
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-brand/{brand_id}")
    public HttpEntity<?> getByBrand(@PathVariable UUID brand_id,
                                    @RequestParam String code) {
        return responseEntityHelper.buildResponse(productService.getByBrand(brand_id, code));
    }

    @CheckPermission("VIEW_PRODUCT")
    @PostMapping("/get-by-branch-and-barcode/{branch_id}")
    public HttpEntity<?> getByBranch(@PathVariable UUID branch_id,
                                     @CurrentUser User user,
                                     @RequestBody ProductBarcodeDto productBarcodeDto) {
        return responseEntityHelper.buildResponse(productService.getByBranchAndBarcode(branch_id, user, productBarcodeDto));
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch/{branch_id}")
    public HttpEntity<?> getByBranchAndBarcode(@PathVariable UUID branch_id) {
        return responseEntityHelper.buildResponse(productService.getByBranch(branch_id));
    }


    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch-for-trade/{branch_id}")
    public HttpEntity<?> getByBranchForTrade(@PathVariable UUID branch_id,
                                             @RequestParam(required = false) UUID category_id,
                                             @RequestParam int page, @RequestParam int size,
                                             @RequestParam(required = false) String searchValue) {
        return responseEntityHelper.buildResponse(productService.getByBranchForTrade(searchValue, branch_id, category_id, page, size));
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch-for-purchase-trade/{branch_id}")
    public HttpEntity<?> getByBranchForSearch(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = productService.getByBranchForSearch(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getByBusiness(@PathVariable UUID business_id, @RequestParam(required = false) UUID branch_id, @RequestParam(required = false) UUID brand_id, @RequestParam(required = false) UUID categoryId) {
        ApiResponse apiResponse = productService.getByBusiness(business_id, branch_id, brand_id, categoryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-business-pageable/{business_id}")
    public HttpEntity<?> getByBusinessPageable(@PathVariable UUID business_id, @RequestParam(required = false) UUID branch_id, @RequestParam(required = false) UUID brand_id, @RequestParam(required = false) UUID categoryId, @RequestParam(required = false) String search, @RequestParam(required = false) String language, @RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = productService.getByBusinessPageableWithTranslations(business_id, branch_id, brand_id, categoryId, search, page, size, language);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-all-by-branch/{branchId}")
    public HttpEntity<?> getByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = productService.getByBranchProduct(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-purchase-product/{branchId}")
    public HttpEntity<?> getPurchaseProduct(@PathVariable UUID branchId, @RequestParam UUID productId, @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page, @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productService.getPurchaseProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-production-product/{branchId}")
    public HttpEntity<?> getProductionProduct(@PathVariable UUID branchId, @RequestParam UUID productId, @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page, @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productService.getProductionProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-trade-product/{branchId}")
    public HttpEntity<?> getTradeProduct(@PathVariable UUID branchId, @RequestParam UUID productId, @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page, @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productService.getTradeProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-content-production-product/{branchId}")
    public HttpEntity<?> getContentProduct(@PathVariable UUID branchId, @RequestParam UUID productId, @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page, @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = productService.getContentProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping("/generate-barcode/{businessId}")
    public HttpEntity<?> generateBarcode(@PathVariable UUID businessId) {
        return responseEntityHelper.buildResponse(productService.generateBarcode(businessId));
    }
}