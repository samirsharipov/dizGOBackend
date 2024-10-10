package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.EmploymentDismissalReport;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.EmploymentDismissalReportRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmploymentDismissalReportService {

    private final EmploymentDismissalReportRepository repository;


    public ApiResponse getInfoOnlyCount(UUID businessId) {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();

        List<EmploymentDismissalReport> reports = repository.findByYear(currentYear, businessId);

        if (reports.isEmpty()) {
            return new ApiResponse("No employment dismissal reports found", false);
        }

        return new ApiResponse("found " + reports.size() + " employment dismissal reports", true, reports);
    }

    public ApiResponse getBetweenYearCount(UUID businessId, int startYear, int endYear) {
        Map<String, long[]> comparisonData = new HashMap<>();

        comparisonData.put(startYear + "", getMonthlyDismissalsData(businessId, startYear));
        comparisonData.put(endYear + "", getMonthlyDismissalsData(businessId, endYear));

        return new ApiResponse("found " + comparisonData.size() + " employment dismissal reports", true, comparisonData);
    }

    public long[] getMonthlyDismissalsData(UUID businessId, int year) {
        long[] monthlyDismissals = new long[12]; // 12 oy uchun
        List<Object[]> results = repository.findMonthlyDismissalsByYear(year, businessId);

        for (Object[] result : results) {
            int month = (int) result[0]; // Oyning raqami
            long dismissedCount = (long) result[1]; // Boshatilgan soni
            monthlyDismissals[month - 1] = dismissedCount; // Oylik sonini to'ldirish
        }

        return monthlyDismissals;
    }
}
