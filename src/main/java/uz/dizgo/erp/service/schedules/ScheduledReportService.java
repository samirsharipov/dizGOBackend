package uz.dizgo.erp.service.schedules;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.EmploymentDismissalReport;
import uz.dizgo.erp.payload.MonthlyEmploymentDismissalReportDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.EmploymentDismissalReportRepository;
import uz.dizgo.erp.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduledReportService {


    private final UserRepository userRepository;

    private final EmploymentDismissalReportRepository reportRepository;
    private final BusinessRepository businessRepository;

//    @Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 0 1 1 * *")// Har oy 1-kuni soat 01:00 da ishga tushadi
    public void generateMonthlyReports() {

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        Timestamp endDate = new Timestamp(System.currentTimeMillis());
        Timestamp startDate = Timestamp.valueOf(oneMonthAgo.atStartOfDay());

        List<UUID> businessIdList = businessRepository.findAllBusinessIds();
        for (UUID businessId : businessIdList) {

            List<MonthlyEmploymentDismissalReportDto> reports =
                    userRepository.getMonthlyReportByBusinessId(businessId, startDate, endDate);

            // Har bir hisobotni saqlash
            for (MonthlyEmploymentDismissalReportDto report : reports) {
                EmploymentDismissalReport employmentReport = new EmploymentDismissalReport();
                employmentReport.setBusinessId(businessId);
                employmentReport.setActiveUsersCount(report.getActiveUsersCount());
                employmentReport.setDismissedUsersCount(report.getDismissedUsersCount());

                reportRepository.save(employmentReport);
            }
        }
    }

}
