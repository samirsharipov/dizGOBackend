package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PaymentTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

    Optional<PaymentTransaction> findByTransactionIdAndActiveFalse(Long transactionId);

    List<PaymentTransaction> findByBranchId(UUID branchId);
}
