package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.CustomerCredit;

import java.util.List;
import java.util.UUID;

public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, UUID> {
    List<CustomerCredit> findByCustomerIdOrderByPaymentDate(UUID customerId);
}