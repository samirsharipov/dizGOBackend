package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Balance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    List<Balance> findAllByBranchId(UUID branchId);
    List<Balance> findAllByBranch_BusinessId(UUID branch_business_id);

    Optional<Balance> findByPaymentMethod_IdAndBranchId(UUID paymentMethod_id, UUID branch_id);

    List<Balance> findAllByPaymentMethodId(UUID paymentMethod_id);
}
