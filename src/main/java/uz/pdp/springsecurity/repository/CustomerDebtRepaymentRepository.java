package uz.pdp.springsecurity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.CustomerDebtRepayment;

import java.util.List;
import java.util.UUID;

public interface CustomerDebtRepaymentRepository extends JpaRepository<CustomerDebtRepayment, UUID> {
    List<CustomerDebtRepayment> findAllByCustomer_Id(UUID customer_id);
}
