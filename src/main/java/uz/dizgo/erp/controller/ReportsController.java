package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.ReportsService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;
    private final ResponseEntityHelper helper;

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/all-by-branch/{branchId}")
    public HttpEntity<?> getAllBranchAmount(@PathVariable UUID branchId,
                                            @RequestParam(required = false) UUID brandId,
                                            @RequestParam(required = false) UUID categoryId,
                                            @RequestParam(required = false) String production) {
        return buildResponse(reportsService.allProductAmount(branchId, brandId, categoryId, production));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/trade-by-branch/{branchId}")
    public HttpEntity<?> getAllTradeProducts(@PathVariable UUID branchId,
                                             @RequestParam(required = false) UUID payMethodId,
                                             @RequestParam(required = false) UUID customerId,
                                             @RequestParam(required = false) Date startDate,
                                             @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.tradeProductByBranch(branchId, payMethodId, customerId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-brand/{branchId}")
    public HttpEntity<?> getAllBrandAmount(@PathVariable UUID branchId,
                                           @RequestParam UUID brandId) {
        return buildResponse(reportsService.allProductByBrand(branchId, brandId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-category/{branchId}")
    public HttpEntity<?> getAllCategoryAmount(@PathVariable UUID branchId,
                                              @RequestParam UUID categoryId) {
        return buildResponse(reportsService.allProductByCategory(branchId, categoryId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-by-project")
    public HttpEntity<?> getAllProject(@RequestParam(required = false) UUID branchId,
                                       @RequestParam(required = false) UUID businessId) {
        return buildResponse(reportsService.projectReport(branchId, businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/amounts-branch")
    public HttpEntity<?> getAllDateByBrand(@RequestParam(required = false) UUID branchId,
                                           @RequestParam(required = false) UUID businessId) {
        return buildResponse(reportsService.allProductAmountByBranch(branchId, businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-sale/{branchId}")
    public HttpEntity<?> mostSaleProducts(@PathVariable UUID branchId,
                                          @RequestParam(required = false) UUID categoryId,
                                          @RequestParam(required = false) UUID brandId,
                                          @RequestParam(required = false) Date startDate,
                                          @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.mostSaleProducts(branchId, categoryId, brandId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-sale/limit")
    public HttpEntity<?> mostSaleProductsPageNation(@RequestParam UUID branchId,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer limit) {
        return buildResponse(reportsService.mostSaleProductsPageNation(branchId, page, limit));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/most-un-sale/{branchId}")
    public HttpEntity<?> mostUnSaleProducts(@PathVariable UUID branchId) {
        return buildResponse(reportsService.mostUnSaleProducts(branchId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/best-selling-product/{branchId}")
    public HttpEntity<?> getBestSellingProduct(@PathVariable UUID branchId,
                                               @RequestParam(required = false) Boolean bySellingAmount,
                                               @RequestParam(required = false) Date startDate,
                                               @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.getBestSellingProduct(bySellingAmount != null, branchId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/purchase/{branchId}")
    public HttpEntity<?> purchaseReports(@PathVariable UUID branchId,
                                         @RequestParam(required = false) UUID supplierId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.purchaseReports(branchId, supplierId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/production/by-date/{branchId}")
    public HttpEntity<?> productionReports(@PathVariable UUID branchId) {
        return buildResponse(reportsService.productionReports(branchId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/delivery/{branchId}")
    public HttpEntity<?> deliveryPriceGet(@PathVariable UUID branchId) {
        return buildResponse(reportsService.deliveryPriceGet(branchId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/outlay/{branchId}")
    public HttpEntity<?> outlayReports(@PathVariable UUID branchId,
                                       @RequestParam(required = false) UUID categoryId,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.outlayReports(branchId, categoryId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/customer/{branchId}")
    public HttpEntity<?> customerReports(@PathVariable UUID branchId,
                                         @RequestParam(required = false) UUID customerId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.customerReports(branchId, customerId, startDate, endDate));
    }

    @GetMapping("/customer/limit")
    public HttpEntity<?> getCustomerByLimit(@RequestParam UUID branch,
                                            @RequestParam(required = false) UUID customerId,
                                            @RequestParam(required = false) Date startDate,
                                            @RequestParam(required = false) Date endDate,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        return buildResponse(reportsService.getCustomerByLimit(branch, customerId, startDate, endDate, page, limit));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-branch/{branchId}")
    public HttpEntity<?> benefitByBranchReports(@PathVariable UUID branchId,
                                                @RequestParam(required = false) String date,
                                                @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return buildResponse(reportsService.dateBenefitAndLostByProductReports(branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-branch")
    public HttpEntity<?> benefitByBranchReportsPageable(@RequestParam UUID branchId,
                                                        @RequestParam(required = false) String date,
                                                        @RequestParam(required = false) Date startDate,
                                                        @RequestParam(required = false) Date endDate,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer limit) {
        return buildResponse(reportsService.benefitByBranchReportsPageable(branchId, date, startDate, endDate, page, limit));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-category/{branchId}")
    public HttpEntity<?> benefitByCategoryReports(@PathVariable UUID branchId,
                                                  @RequestParam(required = false) String date,
                                                  @RequestParam(required = false) Date startDate,
                                                  @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.benefitAndLostByCategoryReports(branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-brand/{branchId}")
    public HttpEntity<?> benefitByBrandReports(@PathVariable UUID branchId,
                                               @RequestParam(required = false) String date,
                                               @RequestParam(required = false) Date startDate,
                                               @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.benefitAndLostByBrandReports(branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/benefit-by-customer/{branchId}")
    public HttpEntity<?> benefitByCustomerReports(@PathVariable UUID branchId,
                                                  @RequestParam(required = false) String date,
                                                  @RequestParam(required = false) Date startDate,
                                                  @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.benefitAndLostByCustomerReports(branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/products-report/{branchId}")
    public HttpEntity<?> productsReport(@PathVariable UUID branchId,
                                        @RequestParam(required = false) UUID customerId,
                                        @RequestParam(required = false) String date,
                                        @RequestParam(required = false) Date startDate,
                                        @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.productsReport(customerId, branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/lid-report/{businessId}")
    public HttpEntity<?> lidReport(@PathVariable UUID businessId) {
        return buildResponse(reportsService.lidReport(businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-best-seller/{businessId}")
    public HttpEntity<?> bestSellerReport(@PathVariable UUID businessId) {
        return buildResponse(reportsService.bestSellerReport(businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-top-supplier/{branchId}")
    public HttpEntity<?> top10Supplier(@PathVariable UUID branchId,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.top10Supplier(branchId, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-trade-by-lid/{businessId}")
    public HttpEntity<?> getLidTradeReport(@PathVariable UUID businessId,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "0") int page) {
        return buildResponse(reportsService.getLidTradeReport(businessId, size, page));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-checkout/{branchId}")
    public HttpEntity<?> getCheckout(@PathVariable UUID branchId,
                                     @RequestParam(required = false) UUID businessId) {
        return buildResponse(reportsService.getCheckout(branchId, businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-increase/{businessId}")
    public HttpEntity<?> getIncrease(@PathVariable UUID businessId,
                                     @RequestParam(required = false) UUID branchId,
                                     @RequestParam(required = false) String date,
                                     @RequestParam(required = false) Date startDate,
                                     @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.getIncrease(businessId, branchId, date, startDate, endDate));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-chart/{branchId}")
    public HttpEntity<?> getChart(@PathVariable UUID branchId,
                                  @RequestParam(required = false) UUID businessId) {
        return buildResponse(reportsService.getChart(branchId, businessId));
    }

    @CheckPermission("VIEW_REPORT")
    @GetMapping("/get-seller-for-chart/{businessId}")
    public HttpEntity<?> getSellerForChart(@PathVariable UUID businessId,
                                           @RequestParam(required = false) UUID branchId) {
        return buildResponse(reportsService.getSellerForChart(branchId, businessId));
    }

    @GetMapping("/outlay/all-info")
    public HttpEntity<?> getAllOutlayInfo(@RequestParam UUID branchId,
                                          @RequestParam(required = false) Date startDate,
                                          @RequestParam(required = false) Date endDate,
                                          @RequestParam(required = false) boolean isDollar,
                                          @CurrentUser User user) {
        return buildResponse(reportsService.getAllOutlayInfo(branchId, startDate, endDate, user, isDollar));
    }

    @GetMapping("/cash-money")
    public HttpEntity<?> getCashManyInfo(@CurrentUser User user,
                                         @RequestParam(required = false) UUID branchId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.getCashManyInfo(user, branchId, startDate, endDate));
    }

    @GetMapping("/trade-count")
    public HttpEntity<?> getTotalTradeCount(@CurrentUser User user,
                                            @RequestParam(required = false) UUID branchId,
                                            @RequestParam(required = false) Date startDate,
                                            @RequestParam(required = false) Date endDate) {
        return buildResponse(reportsService.getTotalTradeCount(user, branchId, startDate, endDate));
    }

    private HttpEntity<ApiResponse> buildResponse(ApiResponse apiResponse) {
        return helper.buildResponse(apiResponse);
    }
}
