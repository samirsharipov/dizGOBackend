package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.ReportsService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    ReportsService reportsService;

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/all-by-branch/{branchId}")
    public HttpEntity<?> getAllBranchAmount(@PathVariable UUID branchId,
                                            @RequestParam(required = false) UUID brandId,
                                            @RequestParam(required = false) UUID categoryId,
                                            @RequestParam(required = false) String production) {
        ApiResponse apiResponse = reportsService.allProductAmount(branchId, brandId, categoryId, production);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/trade-by-branch/{branchId}")
    public HttpEntity<?> getAllTradeProducts(@PathVariable UUID branchId,
                                             @RequestParam(required = false) UUID payMethodId,
                                             @RequestParam(required = false) UUID customerId,
                                             @RequestParam(required = false) Date startDate,
                                             @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.tradeProductByBranch(branchId, payMethodId, customerId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-brand/{branchId}")
    public HttpEntity<?> getAllBrandAmount(@PathVariable UUID branchId,
                                           @RequestParam UUID brandId) {
        ApiResponse apiResponse = reportsService.allProductByBrand(branchId, brandId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-category/{branchId}/{categoryId}")
    public HttpEntity<?> getAllCategoryAmount(@PathVariable UUID branchId,
                                              @RequestParam UUID categoryId) {
        ApiResponse apiResponse = reportsService.allProductByCategory(branchId, categoryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-project")
    public HttpEntity<?> getAllProject(@RequestParam(required = false) UUID branchId,
                                       @RequestParam(required = false) UUID businessId) {
        ApiResponse apiResponse = reportsService.projectReport(branchId, businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-branch")
    public HttpEntity<?> getAllDateByBrand(@RequestParam(required = false) UUID branchId,
                                           @RequestParam(required = false) UUID businessId) {
        ApiResponse apiResponse = reportsService.allProductAmountByBranch(branchId, businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-sale/{branchId}")
    public HttpEntity<?> mostSaleProducts(@PathVariable UUID branchId,
                                          @RequestParam(required = false) UUID categoryId,
                                          @RequestParam(required = false) UUID brandId,
                                          @RequestParam(required = false) Date startDate,
                                          @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.mostSaleProducts(branchId, categoryId, brandId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-sale/limit")
    public HttpEntity<?> mostSaleProductsPageNation(@RequestParam UUID branchId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        ApiResponse apiResponse = reportsService.mostSaleProductsPageNation(branchId, page, limit);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-un-sale/{branchId}")
    public HttpEntity<?> mostUnSaleProducts(@PathVariable UUID branchId) {
        ApiResponse apiResponse = reportsService.mostUnSaleProducts(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/best-selling-product/{branchId}")
    public HttpEntity<?> getBestSellingProduct(@PathVariable UUID branchId, @RequestParam(required = false) Boolean bySellingAmount, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.getBestSellingProduct(bySellingAmount != null, branchId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/purchase/{branchId}")
    public HttpEntity<?> purchaseReports(@PathVariable UUID branchId,
                                         @RequestParam(required = false) UUID supplierId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.purchaseReports(branchId, supplierId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/production/by-date/{branchId}")
    public HttpEntity<?> productionReports(@PathVariable UUID branchId) {
        ApiResponse apiResponse = reportsService.productionReports(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/delivery/{branchId}")
    public HttpEntity<?> deliveryPriceGet(@PathVariable UUID branchId) {
        ApiResponse apiResponse = reportsService.deliveryPriceGet(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/outlay/{branchId}")
    public HttpEntity<?> outlayReports(@PathVariable UUID branchId,
                                       @RequestParam(required = false) UUID categoryId,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.outlayReports(branchId, categoryId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/customer/{branchId}")
    public HttpEntity<?> customerReports(@PathVariable UUID branchId,
                                         @RequestParam(required = false) UUID customerId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.customerReports(branchId, customerId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/customer/limit")
    public HttpEntity<?> getCustomerByLimit(@RequestParam UUID branch,
                                            @RequestParam(required = false) UUID customerId,
                                            @RequestParam(required = false) Date startDate,
                                            @RequestParam(required = false) Date endDate,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        ApiResponse apiResponse = reportsService.getCustomerByLimit(branch, customerId, startDate, endDate, page, limit);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-branch/{branchId}")
    public HttpEntity<?> benefitByBranchReports(@PathVariable UUID branchId,
                                                @RequestParam(required = false) String date,
                                                @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
    {


        ApiResponse apiResponse = reportsService.dateBenefitAndLostByProductReports(branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }




    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-branch")
    public HttpEntity<?> benefitByBranchReportsPageable(@RequestParam UUID branchId,
                                                        @RequestParam(required = false) String date,
                                                        @RequestParam(required = false) Date startDate,
                                                        @RequestParam(required = false) Date endDate,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer limit) {
        ApiResponse apiResponse = reportsService.benefitByBranchReportsPageable(branchId, date, startDate, endDate, page, limit);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-category/{branchId}")
    public HttpEntity<?> benefitByCategoryReports(@PathVariable UUID branchId,
                                                  @RequestParam(required = false) String date,
                                                  @RequestParam(required = false) Date startDate,
                                                  @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.benefitAndLostByCategoryReports(branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-brand/{branchId}")
    public HttpEntity<?> benefitByBrandReports(@PathVariable UUID branchId,
                                               @RequestParam(required = false) String date,
                                               @RequestParam(required = false) Date startDate,
                                               @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.benefitAndLostByBrandReports(branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-customer/{branchId}")
    public HttpEntity<?> benefitByCustomerReports(@PathVariable UUID branchId,
                                                  @RequestParam(required = false) String date,
                                                  @RequestParam(required = false) Date startDate,
                                                  @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.benefitAndLostByCustomerReports(branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/products-report/{branchId}")
    public HttpEntity<?> productsReport(@PathVariable UUID branchId,
                                        @RequestParam(required = false) UUID customerId,
                                        @RequestParam(required = false) String date,
                                        @RequestParam(required = false) Date startDate,
                                        @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.productsReport(customerId, branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/lid-report/{businessId}")
    public HttpEntity<?> lidReport(@PathVariable UUID businessId) {
        ApiResponse apiResponse = reportsService.lidReport(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-best-seller/{businessId}")
    public HttpEntity<?> bestSellerReport(@PathVariable UUID businessId) {
        ApiResponse apiResponse = reportsService.bestSellerReport(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-top-supplier/{branchId}")
    public HttpEntity<?> top10Supplier(@PathVariable UUID branchId,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.top10Supplier(branchId, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-trade-by-lid/{businessId}")
    public HttpEntity<?> getLidTradeReport(@PathVariable UUID businessId,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "0") int page) {
        ApiResponse apiResponse = reportsService.getLidTradeReport(businessId, size, page);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-checkout/{branchId}")
    public HttpEntity<?> getCheckout(@PathVariable UUID branchId,
                                     @RequestParam(required = false) UUID businessId) {
        ApiResponse apiResponse = reportsService.getCheckout(branchId, businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-increase/{businessId}")
    public HttpEntity<?> getIncrease(@PathVariable UUID businessId,
                                     @RequestParam(required = false) UUID branchId,
                                     @RequestParam(required = false) String date,
                                     @RequestParam(required = false) Date startDate,
                                     @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = reportsService.getIncrease(businessId, branchId, date, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-chart/{branchId}")
    public HttpEntity<?> getChart(@PathVariable UUID branchId,
                                  @RequestParam(required = false) UUID businessId) {
        ApiResponse apiResponse = reportsService.getChart(branchId, businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-seller-for-chart/{businessId}")
    public HttpEntity<?> getSellerForChart(@PathVariable UUID businessId,
                                           @RequestParam(required = false) UUID branchId) {
        ApiResponse apiResponse = reportsService.getSellerForChart(branchId, businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/outlay/all-info")
    public HttpEntity<?> getAllOutlayInfo(@RequestParam UUID branchId, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate, @RequestParam(required = false) boolean isDollar, @CurrentUser User user) {
        ApiResponse apiResponse = reportsService.getAllOutlayInfo(branchId, startDate, endDate,user, isDollar);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @GetMapping("/cash-money")
    public HttpEntity<?> getCashManyInfo(@CurrentUser User user,@RequestParam(required = false) UUID branchId,@RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate){
        ApiResponse apiResponse = reportsService.getCashManyInfo(user,branchId,startDate,endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @GetMapping("/trade-count")
    public HttpEntity<?> getTotalTradeCount(@CurrentUser User user,@RequestParam(required = false) UUID branchId,@RequestParam(required = false) Date startDate,@RequestParam(required = false) Date endDate){
        ApiResponse apiResponse = reportsService.getTotalTradeCount(user,branchId,startDate,endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
