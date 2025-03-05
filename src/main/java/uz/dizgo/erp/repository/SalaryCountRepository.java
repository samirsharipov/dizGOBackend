package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.SalaryCount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryCountRepository extends JpaRepository<SalaryCount, UUID> {
    List<SalaryCount> findAllByAgreement_UserIdAndBranch_Id(UUID userId, UUID branchId);

    Optional<SalaryCount> findByAgreementIdAndBranchId(UUID agreementId, UUID branchId);

    void deleteAllByAgreement_UserIdAndBranchId(UUID userID, UUID branchId);
}
