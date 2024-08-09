package uz.pdp.springsecurity.shoxjaxon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.shoxjaxon.repository.SotuvRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class SotuvService {
    private final SotuvRepository sotuvRepository;
    private final Logger logger = LoggerFactory.getLogger(SotuvService.class.getName());

    public ApiResponse getTotalSum(UUID businessId, Date startDate, Date endDate) {
        try {
            BigDecimal[] totalSumsPaid, totalSumsDebt;
            if (startDate == null && endDate == null) {
                totalSumsPaid = sotuvRepository.calculateTotalSumForBranchWithDateRange(businessId);
                totalSumsDebt = sotuvRepository.calculateTotalDebtSumForBranchWithDateRange(businessId);
            } else {
                totalSumsPaid = sotuvRepository.calculateTotalSumForBranchWithDateRangeFiltered(businessId, startDate, endDate);
                totalSumsDebt = sotuvRepository.calculateTotalDebtSumForBranchWithDateRangeFiltered(businessId, startDate, endDate);
            }

            if (totalSumsPaid == null || totalSumsPaid.length < 2 || totalSumsDebt == null || totalSumsDebt.length < 2) {
                logger.error("Unexpected error calculating total sum for businessId: " + businessId);
                return new ApiResponse("Unexpected error calculating total sum for businessId: " + businessId, false);
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

            return new ApiResponse("succes", true, result);
        } catch (Exception e) {
            logger.error("Unexpected error calculating total sum for businessId: " + businessId, e);
            return new ApiResponse("Unexpected error calculating total sum for businessId: " + businessId, false);
        }
    }
}
