package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Balance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    List<Balance> findAllByBranchId(UUID branchId);
    List<Balance> findAllByBranchIdAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(UUID branch_id, String currency, String paymentMethod_type);
    List<Balance> findAllByBranch_Business_idAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(UUID branch_business_id, String currency, String paymentMethod_type);
    List<Balance> findAllByBranch_BusinessId(UUID branch_business_id);
    Optional<Balance> findByPaymentMethod_IdAndBranchIdAndCurrencyIgnoreCase(UUID paymentMethod_id, UUID branch_id, String currency);
    List<Balance> findAllByPaymentMethodIdAndCurrencyIgnoreCase(UUID paymentMethod_id, String currency);
    boolean existsAllByBranch_Business_IdAndCurrencyIgnoreCase(UUID branch_business_id, String currency);
}
