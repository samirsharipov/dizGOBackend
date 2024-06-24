package uz.pdp.springsecurity.shoxjaxon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.shoxjaxon.repository.SotuvRepositiry;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/businessTotalSums")
public class SotuvController {

    private final SotuvRepositiry sotuvRepositiry;

    @Autowired
    public SotuvController(SotuvRepositiry sotuvRepositiry) {
        this.sotuvRepositiry = sotuvRepositiry;
    }



    private static final Logger logger = LoggerFactory.getLogger(SotuvController.class);

    @GetMapping("/businessTotalSum")
    public ResponseEntity<?> getBusinessTotalSum(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal[] totalSumsPaid, totalSumsDebt;
            if (startDate == null && endDate == null) {
                totalSumsPaid = sotuvRepositiry.calculateTotalSumForBranchWithDateRange(businessId);
                totalSumsDebt = sotuvRepositiry.calculateTotalDebtSumForBranchWithDateRange(businessId);
            } else {
                totalSumsPaid = sotuvRepositiry.calculateTotalSumForBranchWithDateRangeFiltered(businessId, startDate, endDate);
                totalSumsDebt = sotuvRepositiry.calculateTotalDebtSumForBranchWithDateRangeFiltered(businessId, startDate, endDate);
            }

            if (totalSumsPaid == null || totalSumsPaid.length < 2 || totalSumsDebt == null || totalSumsDebt.length < 2) {
                logger.error("Unexpected error calculating total sum for businessId: " + businessId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected error calculating total sum for businessId: " + businessId);
            }

            BigDecimal totalSumPaidSum = totalSumsPaid[0] != null ? totalSumsPaid[0] : BigDecimal.ZERO;
            BigDecimal totalSumPaidSumDollar = totalSumsPaid[1] != null ? totalSumsPaid[1] : BigDecimal.ZERO;
            BigDecimal totalSumDebtSum = totalSumsDebt[0] != null ? totalSumsDebt[0] : BigDecimal.ZERO;
            BigDecimal totalSumDebtSumDollar = totalSumsDebt[1] != null ? totalSumsDebt[1] : BigDecimal.ZERO;

            Map<String, BigDecimal> result = Map.of(
                    "totalSumPaidSum", totalSumPaidSum,
                    "totalSumPaidSumDollar", totalSumPaidSumDollar,
                    "totalSumDebtSum", totalSumDebtSum,
                    "totalSumDebtSumDollar", totalSumDebtSumDollar
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }

    @GetMapping("/businessTotalFoyda")
    public HttpEntity<?> getBusinessTotalFoyda(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalFoyda;

            if (startDate == null && endDate == null) {
                totalFoyda = sotuvRepositiry.calculateTotalFoydaForBranch(businessId);
            } else {
                totalFoyda = sotuvRepositiry.calculateTotalFoydaForBranchWithDateRange(businessId, startDate, endDate);
            }

            return ResponseEntity.ok(totalFoyda);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }

    @GetMapping("/xarajatForBranch")
    public ResponseEntity<BigDecimal> getXarajatForBranchWithDateRange(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalXarajat;

            if (startDate == null && endDate == null) {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranch(branchId);
            } else {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranchWithDateRange(branchId, startDate, endDate);
            }

            return ResponseEntity.ok(totalXarajat);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/dollarXarajatForBranch")
    public ResponseEntity<BigDecimal> getDollarXarajatForBranchWithDateRange(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalDollarXarajat;

            if (startDate == null && endDate == null) {
                totalDollarXarajat = sotuvRepositiry.calculateTotalDollarXarajatForBranch(branchId);
            } else {
                totalDollarXarajat = sotuvRepositiry.calculateTotalDollarXarajatForBranchWithDateRange(branchId, startDate, endDate);
            }

            return ResponseEntity.ok(totalDollarXarajat);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/businessTotalXarajat")
    public ResponseEntity<?> getBusinessTotalXarajat(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalXarajat;
            BigDecimal totalDollarXarajat;
            if (startDate == null && endDate == null) {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranch(businessId);
                totalDollarXarajat = sotuvRepositiry.calculateTotalDollarXarajatForBranch(businessId);
            } else {
                totalXarajat = sotuvRepositiry.calculateTotalXarajatForBranchWithDateRange(businessId, startDate, endDate);
                totalDollarXarajat = sotuvRepositiry.calculateTotalDollarXarajatForBranchWithDateRange(businessId, startDate, endDate);
            }

            Map<String, BigDecimal> result = Map.of(
                    "totalXarajat", totalXarajat != null ? totalXarajat : BigDecimal.ZERO,
                    "totalDollarXarajat", totalDollarXarajat != null ? totalDollarXarajat : BigDecimal.ZERO
            );

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total xarajat for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total xarajat for businessId: " + businessId);
        }
    }

    @GetMapping("/xarajatForBusiness")
    public ResponseEntity<BigDecimal> getXarajatForBusinessWithDateRange(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return null;
    }

    @GetMapping("/outlaySumsByPaymentMethod")
    public ResponseEntity<?> getOutlaySumsByPaymentMethod(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Map<String, Object>> outlaySums;
            if (startDate != null && endDate != null) {
                outlaySums = sotuvRepositiry.getOutlaySumsByPaymentMethod(branchId, startDate, endDate);
            } else {
                outlaySums = sotuvRepositiry.getOutlaySumsByPaymentMethod(branchId, null, null);
            }
            return ResponseEntity.ok(outlaySums);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving outlay sums by payment method for branchId: " + branchId, e);
            return ResponseEntity.status(500).body("Unexpected error retrieving outlay sums by payment method for branchId: " + branchId);
        }
    }

    @GetMapping("/tradeSumsByPaymentMethod")
    public ResponseEntity<?> getTradeSumsByPaymentMethod(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Map<String, Object>> tradeSums;
            if (startDate != null && endDate != null) {
                tradeSums = sotuvRepositiry.getTradeSumsByPaymentMethod(branchId, startDate, endDate);
            } else {
                tradeSums = sotuvRepositiry.getTradeSumsByPaymentMethod(branchId, null, null);
            }
            return ResponseEntity.ok(tradeSums);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving trade sums by payment method for branchId: " + branchId, e);
            return ResponseEntity.status(500).body("Unexpected error retrieving trade sums by payment method for branchId: " + branchId);
        }
    }

    @GetMapping("/purchaseSumsByPaymentMethod")
    public ResponseEntity<?> getPurchaseSumsByPaymentMethod(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Map<String, Object>> purchaseSums;
            if (startDate != null && endDate != null) {
                purchaseSums = sotuvRepositiry.getPurchaseSumsByPaymentMethod(branchId, startDate, endDate);
            } else {
                purchaseSums = sotuvRepositiry.getPurchaseSumsByPaymentMethod(branchId, null, null);
            }
            return ResponseEntity.ok(purchaseSums);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving purchase sums by payment method for branchId: " + branchId, e);
            return ResponseEntity.status(500).body("Unexpected error retrieving purchase sums by payment method for branchId: " + branchId);
        }
    }

    @GetMapping("/businessTotalPurchase")
    public HttpEntity<?> getBusinessTotalPurchase(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalPurchase;
            if (startDate == null && endDate == null) {
                totalPurchase = sotuvRepositiry.calculateTotalPurchaseSumForBranch(businessId);
            } else {
                totalPurchase = sotuvRepositiry.calculateTotalPurchaseSumForBranchWithDateRange(businessId, startDate, endDate);
            }
            return ResponseEntity.ok(totalPurchase);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total purchase for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total purchase for businessId: " + businessId);
        }
    }

    @GetMapping("/businessTotalRepaymentDebt")
    public HttpEntity<?> getBusinessTotalRepaymentDebt(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalRepaymentDebt;
            if (startDate == null && endDate == null) {
                totalRepaymentDebt = sotuvRepositiry.calculateTotalRepaymentDebtSumForBranchWithDateRange(businessId, null, null);
            } else {
                totalRepaymentDebt = sotuvRepositiry.calculateTotalRepaymentDebtSumForBranchWithDateRange(businessId, startDate, endDate);
            }
            return ResponseEntity.ok(totalRepaymentDebt);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total repayment debt for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total repayment debt for businessId: " + businessId);
        }
    }

    @GetMapping("/repaymentDebtSumsByPaymentMethod")
    public ResponseEntity<?> getRepaymentDebtSumsByPaymentMethod(
            @RequestParam(name = "branchId", required = true) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Map<String, Object>> repaymentDebtSums = sotuvRepositiry.getRepaymentDebtSumsByPaymentMethod(branchId, startDate, endDate);
            return ResponseEntity.ok(repaymentDebtSums);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving repayment debt sums by payment method for branchId: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error retrieving repayment debt sums by payment method for branchId: " + branchId);
        }
    }

    @GetMapping("/repaymentDebtSumsByPaymentMethodWithDollar")
    public ResponseEntity<?> getRepaymentDebtSumsByPaymentMethodWithDollar(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "branchId", required = false) UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            Map<String, Object> repaymentDebtSums = sotuvRepositiry.getRepaymentDebtSumsByPaymentMethodWithDollar(businessId, branchId, startDate, endDate);
            return ResponseEntity.ok(repaymentDebtSums);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving repayment debt sums by payment method for businessId: " + businessId + ", branchId: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error retrieving repayment debt sums by payment method for businessId: " + businessId + ", branchId: " + branchId);
        }
    }



    @GetMapping("/debtCanculsByPaymentMethod")
    public ResponseEntity<?> getDebtCanculsByPaymentMethod(
            @RequestParam(name = "branchId") UUID branchId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Map<String, Object>> debtCanculs = sotuvRepositiry.getDebtCanculsByPaymentMethod(branchId, startDate, endDate);
            return ResponseEntity.ok(debtCanculs);
        } catch (Exception e) {
            logger.error("Unexpected error getting debt cancels by payment method for branchId: " + branchId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error getting debt cancels by payment method for branchId: " + branchId);
        }
    }

    @GetMapping("/businessTotalSummm")
    public ResponseEntity<?> getBusinessTotalSummm(
            @RequestParam(name = "businessId", required = true) UUID businessId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            BigDecimal totalSum;
            if (startDate == null && endDate == null) {
                totalSum = sotuvRepositiry.calculateTotalSumForBranch(businessId);
            } else {
                totalSum = sotuvRepositiry.calculateTotalSumForBranchWithDateRange(businessId, startDate, endDate);
            }

            if (totalSum == null) {
                logger.error("Unexpected error calculating total sum for businessId: " + businessId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected error calculating total sum for businessId: " + businessId);
            }

            Map<String, BigDecimal> result = Map.of("totalSum", totalSum);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error calculating total sum for businessId: " + businessId);
        }
    }


}
