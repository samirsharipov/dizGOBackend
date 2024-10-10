package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.EmploymentDismissalReport;

import java.util.List;
import java.util.UUID;

public interface EmploymentDismissalReportRepository extends JpaRepository<EmploymentDismissalReport, UUID> {

    @Query("SELECT e FROM EmploymentDismissalReport e WHERE YEAR(e.createdAt) = :year AND e.businessId = :businessId")
    List<EmploymentDismissalReport> findByYear(@Param("year") int year, @Param("businessId") UUID businessId);

    @Query("SELECT MONTH(e.createdAt) AS month, SUM(e.dismissedUsersCount) AS dismissedCount " +
            "FROM EmploymentDismissalReport e " +
            "WHERE YEAR(e.createdAt) = :year AND e.businessId = :businessId " +
            "GROUP BY MONTH(e.createdAt)")
    List<Object[]> findMonthlyDismissalsByYear(@Param("year") int year, @Param("businessId") UUID businessId);

}
