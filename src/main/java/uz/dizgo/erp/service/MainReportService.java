package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.MainPageReport;
import uz.dizgo.erp.repository.*;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainReportService {

    private final TradeRepository tradeRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final OutlayRepository outlayRepository;
    private final PurchaseRepository purchaseRepository;

    public ApiResponse getMainRepost(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        MainPageReport report = new MainPageReport();
        report.setTotalTradeSum(totalTradeSum(businessId, branchId, startDate, endDate));
        report.setTotalTradeProfitSum(totalTradeProfitSum(businessId, branchId, startDate, endDate));
        report.setTotalTradeCount(totalTradeCount(businessId, branchId, startDate, endDate));
        report.setTotalTradeCustomerCount(totalCustomerCount(businessId, branchId));
        report.setTotalProductCount(countProduct(businessId, branchId));
        report.setTotalProductCountGreaterThanZero(countProductGreaterThanZero(businessId, branchId));
        report.setTotalProductCountLessThanZero(countProductLessThanZero(businessId, branchId));
        report.setTotalProductMinQuantity(minQuantity(businessId, branchId));
        report.setCountUsers(countUsers(businessId, branchId));
        report.setOutlayTotalSum(totalOutlaySum(businessId, branchId, startDate, endDate));
        report.setPurchaseCount(totalPurchaseCount(businessId, branchId, startDate, endDate));

        Double totalProductSalePrice = totalProductSalePrice(businessId, branchId, startDate, endDate);
        report.setTotalProductSalePrice(totalProductSalePrice != null ? totalProductSalePrice : 0.0);
        Double totalProductBuyPrice = totalProductBuyPrice(businessId, branchId, startDate, endDate);
        report.setTotalProductPurchasePrice(totalProductBuyPrice != null ? totalProductBuyPrice : 0.0);

        return new ApiResponse("all", true, report);
    }

    // Jami savdo summasi
    private Double totalTradeSum(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        Double totalTradeSum = 0.0;
        if (branchId != null) {
            Double totalSum = tradeRepository.totalSum(branchId, startDate, endDate);
            totalTradeSum += totalSum != null ? totalSum : 0.0;
        } else {
            Double totalSumByBusiness = tradeRepository.totalSumByBusiness(businessId, startDate, endDate);
            return totalSumByBusiness != null ? totalSumByBusiness : 0.0;
        }
        return totalTradeSum;
    }

    //Jami savdo foyda summasi
    private Double totalTradeProfitSum(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        Double totalTradeProfitSum = 0.0;
        if (branchId != null) {
            Double totalProfitByBranchId = tradeRepository.totalProfit(branchId, startDate, endDate);
            totalTradeProfitSum += totalProfitByBranchId != null ? totalProfitByBranchId : 0.0;
        } else {
            Double totalProfitByBusinessId = tradeRepository.totalProfitByBusinessId(businessId, startDate, endDate);
            return totalProfitByBusinessId != null ? totalProfitByBusinessId : 0.0;
        }
        return totalTradeProfitSum;
    }

    private long totalTradeCount(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        long totalTrade = 0L;
        if (branchId != null) {
            Long countedAllByBranch = tradeRepository.countAllByBranchId(branchId, startDate, endDate);
            totalTrade += countedAllByBranch != null ? countedAllByBranch : 0L;
        } else {
            Long countedAllByBusiness = tradeRepository.countAllByBusinessId(businessId, startDate, endDate);
            totalTrade += countedAllByBusiness != null ? countedAllByBusiness : 0L;
        }
        return totalTrade;
    }

    // count customer district in trade
    private long totalCustomerCount(UUID businessId, UUID branchId) {
        long totalCustomers = 0L;
        if (branchId != null) {
            Long distinctCustomersByBranchId = tradeRepository.countDistinctCustomersByBranchId(branchId);
            totalCustomers += distinctCustomersByBranchId != null ? distinctCustomersByBranchId : 0L;
        } else {
            Long customersByBusinessId = tradeRepository.countDistinctCustomersByBusinessId(businessId);
            totalCustomers += customersByBusinessId != null ? customersByBusinessId : 0L;
        }
        return totalCustomers;
    }


    private long countProduct(UUID businessId, UUID branchId) {
        long productCount = 0L;
        if (branchId != null) {
            Long countProductsByBranch = productRepository.countProductsByBranch(branchId);
            productCount += countProductsByBranch != null ? countProductsByBranch : 0L;
        } else {
            Long productsByBusiness = productRepository.countProductsByBusiness(businessId);
            productCount += productsByBusiness != null ? productsByBusiness : 0L;
        }
        return productCount;
    }

    private long countProductGreaterThanZero(UUID businessId, UUID branchId) {
        long productCount = 0L;
        if (branchId != null) {
            Long count = warehouseRepository.countProductsWithAmountGreaterThanZeroByBranch(branchId);
            productCount += count != null ? count : 0L;
        } else {
            Long count = warehouseRepository.countProductsWithAmountGreaterThanZeroByBusiness(businessId);
            productCount += count != null ? count : 0L;
        }
        return productCount;
    }

    private long countProductLessThanZero(UUID businessId, UUID branchId) {
        long productCount = 0L;
        if (branchId != null) {
            Long counted = warehouseRepository.countProductsWithAmountLessThanOrEqualToZeroByBranch(branchId);
            productCount += counted != null ? counted : 0L;
        } else {
            Long count = warehouseRepository.countProductsWithAmountLessThanOrEqualToZeroByBusiness(businessId);
            productCount += count != null ? count : 0L;
        }
        return productCount;
    }

    private long minQuantity(UUID businessId, UUID branchId) {
        long productCount = 0L;
        if (branchId != null) {
            Long counted = warehouseRepository.countProductsWithAmountLessThanOrEqualToMinQuantityByBranch(branchId);
            productCount += counted != null ? counted : 0L;
        } else {
            Long count = warehouseRepository.countProductsWithAmountLessThanOrEqualToMinQuantityByBusiness(businessId);
            productCount += count != null ? count : 0L;
        }
        return productCount;
    }

    private long countUsers(UUID businessId, UUID branchId) {
        long userCount = 0L;
        if (branchId != null) {
            Long counted = userRepository.countActiveUsersByBranch(branchId);
            userCount += counted != null ? counted : 0L;
        } else {
            Long count = userRepository.countActiveUsersByBusiness(businessId);
            userCount += count != null ? count : 0L;
        }
        return userCount;
    }

    private Double totalOutlaySum(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        Double totalOutlaySum = 0.0;
        if (branchId != null) {
            Double total = outlayRepository.outlayByBranchIdAndCreatedAtBetween(branchId, startDate, endDate);
            totalOutlaySum += total != null ? total : 0.0;
        } else {
            Double total = outlayRepository.outlayByCreatedAtBetweenAndBusinessId(businessId, startDate, endDate);
            totalOutlaySum += total != null ? total : 0.0;
        }
        return totalOutlaySum;
    }

    private long totalPurchaseCount(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        long purchaseCount = 0L;
        if (branchId != null) {
            Long counted = purchaseRepository.countPurchasesByBranchIdAndDateBetween(branchId, startDate, endDate);
            purchaseCount += counted != null ? counted : 0L;
        } else {
            Long count = purchaseRepository.countPurchasesByBusinessIdAndDateBetween(businessId, startDate, endDate);
            purchaseCount += count != null ? count : 0L;
        }
        return purchaseCount;
    }

    private Double totalProductSalePrice(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        Double productSalePrice = 0.0;
        if (branchId != null) {
            productSalePrice = productRepository.totalProductSalePriceByBranch(branchId, startDate, endDate);
        } else {
            productSalePrice = productRepository.totalProductSalePrice(businessId, startDate, endDate);
        }
        return productSalePrice;
    }

    private Double totalProductBuyPrice(UUID businessId, UUID branchId, Timestamp startDate, Timestamp endDate) {
        Double productBuyPrice = 0.0;
        if (branchId != null) {
            productBuyPrice = productRepository.totalProductBuyPriceByBranch(branchId, startDate, endDate);
        } else {
            productBuyPrice = productRepository.totalProductBuyPrice(businessId, startDate, endDate);
        }
        return productBuyPrice;
    }
}