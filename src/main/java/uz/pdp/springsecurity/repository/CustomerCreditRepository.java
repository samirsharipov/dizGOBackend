package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.CustomerCredit;

import java.util.List;
import java.util.UUID;

public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, UUID> {
    List<CustomerCredit> findByCustomerIdOrderByPaymentDate(UUID customerId);
}