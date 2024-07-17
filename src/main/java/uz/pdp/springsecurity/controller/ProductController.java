package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ProductBarcodeDto;
import uz.pdp.springsecurity.payload.ProductDto;
import uz.pdp.springsecurity.payload.ProductIdsDto;
import uz.pdp.springsecurity.service.ProductService;
import uz.pdp.springsecurity.service.ProductTypeService;
import uz.pdp.springsecurity.utils.AppConstant;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private ProductTypeService productTypePriceService;


    @CheckPermission("ADD_PRODUCT")
    @PostMapping()
    public HttpEntity<?> add(@Valid @RequestBody ProductDto productDto, @CurrentUser User user) throws ParseException {
        ApiResponse apiResponse = productService.addProduct(productDto, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_PRODUCT")
    @PutMapping("{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ProductDto productDto, @CurrentUser User user) {
        ApiResponse apiResponse = productService.editProduct(id, productDto, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = productService.getProduct(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PRODUCT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOne(@PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = productService.deleteProduct(id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("DELETE_PRODUCT")
    @DeleteMapping("/delete-few")
    public HttpEntity<?> deleteFew(@RequestBody ProductIdsDto productIdsDto) {
        ApiResponse apiResponse = productService.deleteProducts(productIdsDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PRODUCT")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateProduct(@PathVariable UUID id) {
        System.out.println("Deactivating product with ID: " + id); // Tekshiruv
        productService.deactivateProduct(id);
        return ResponseEntity.ok("Product deactivated successfully");
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-barcode/{barcode}")
    public HttpEntity<?> getByBarcode(@PathVariable String barcode, @CurrentUser User user) {
        ApiResponse apiResponse = productService.getByBarcode(barcode, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping
    public HttpEntity<?> get(@CurrentUser User user) {
        ApiResponse apiResponse = productService.getAll(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-category/{category_id}")
    public HttpEntity<?> getByCategory(@PathVariable UUID category_id, @CurrentUser User user) {
        ApiResponse apiResponse = productService.getByCategory(category_id, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-brand/{brand_id}")
    public HttpEntity<?> getByBrand(@PathVariable UUID brand_id) {
        ApiResponse apiResponse = productService.getByBrand(brand_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @PostMapping("/get-by-branch-and-barcode/{branch_id}")
    public HttpEntity<?> getByBranch(@PathVariable UUID branch_id, @CurrentUser User user, @RequestBody ProductBarcodeDto productBarcodeDto) {
        ApiResponse apiResponse = productService.getByBranchAndBarcode(branch_id, user, productBarcodeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch/{branch_id}")
    public HttpEntity<?> getByBranchAndBarcode(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = productService.getByBranch(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/search/{branch_id}")
    public HttpEntity<?> search(@PathVariable UUID branch_id,
                                @RequestParam String name) {
        ApiResponse apiResponse = productService.search(branch_id, name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch-for-trade/{branch_id}")
    public HttpEntity<?> getByBranchForTrade(@PathVariable UUID branch_id,@RequestParam(required = false) UUID category_id, @RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "") String searchValue) {
        ApiResponse apiResponse = productService.getByBranchForTrade(searchValue, branch_id, category_id, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-branch-for-purchase-trade/{branch_id}")
    public HttpEntity<?> getByBranchForSearch(@PathVariable UUID branch_id) {
        ApiResponse apiResponse = productService.getByBranchForSearch(branch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getByBusiness(@PathVariable UUID business_id,
                                       @RequestParam(required = false) UUID branch_id,
                                       @RequestParam(required = false) UUID brand_id,
                                       @RequestParam(required = false) UUID categoryId
    ) {
        ApiResponse apiResponse = productService.getByBusiness(business_id, branch_id, brand_id, categoryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-by-business-pageable/{business_id}")
    public HttpEntity<?> getByBusinessPageable(@PathVariable UUID business_id,
                                               @RequestParam(required = false) UUID branch_id,
                                               @RequestParam(required = false) UUID brand_id,
                                               @RequestParam(required = false) UUID categoryId,
                                               @RequestParam(required = false) String search,
                                               @RequestParam(defaultValue = "0", required = false) int page,
                                               @RequestParam(defaultValue = "10", required = false) int size
    ) {
        ApiResponse apiResponse = productService.getByBusinessPageable(business_id, branch_id, brand_id, categoryId, search, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-all-by-branch/{branchId}")
    public HttpEntity<?> getByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = productService.getByBranchProduct(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-purchase-product/{branchId}")
    public HttpEntity<?> getPurchaseProduct(@PathVariable UUID branchId,
                                            @RequestParam UUID productId,
                                            @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                            @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size
    ) {
        ApiResponse apiResponse = productService.getPurchaseProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-production-product/{branchId}")
    public HttpEntity<?> getProductionProduct(@PathVariable UUID branchId,
                                              @RequestParam UUID productId,
                                              @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                              @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size
    ) {
        ApiResponse apiResponse = productService.getProductionProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-trade-product/{branchId}")
    public HttpEntity<?> getTradeProduct(@PathVariable UUID branchId,
                                         @RequestParam UUID productId,
                                         @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size
    ) {
        ApiResponse apiResponse = productService.getTradeProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PRODUCT")
    @GetMapping("/get-content-production-product/{branchId}")
    public HttpEntity<?> getContentProduct(@PathVariable UUID branchId,
                                           @RequestParam UUID productId,
                                           @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                           @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size
    ) {
        ApiResponse apiResponse = productService.getContentProduct(branchId, productId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
