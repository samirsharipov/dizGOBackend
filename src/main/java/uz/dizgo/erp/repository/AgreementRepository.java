package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Agreement;
import uz.dizgo.erp.enums.SalaryStatus;
import uz.dizgo.erp.payload.AgreementGetDto;

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
