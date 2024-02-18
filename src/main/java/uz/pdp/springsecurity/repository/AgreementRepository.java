package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Agreement;
import uz.pdp.springsecurity.enums.SalaryStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgreementRepository extends JpaRepository<Agreement, UUID> {
    List<Agreement> findAllByUserId(UUID userId);

    Optional<Agreement> findByUserIdAndSalaryStatus(UUID userId, SalaryStatus salaryStatus);

    Optional<Agreement> findByUserIdAndSalaryStatusAndActiveTrue(UUID userId, SalaryStatus salaryStatus);

    List<Agreement> findAllByUser_BusinessIdAndSalaryStatusAndEndDateBeforeAndActiveTrue(UUID businessId, SalaryStatus salaryStatus, Date todayEnd);

    Integer countAllByUserId(UUID userId);
}
