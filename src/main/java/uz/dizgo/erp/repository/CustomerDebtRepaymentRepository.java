package uz.dizgo.erp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.CustomerDebtRepayment;

import java.util.List;
import java.util.UUID;

public interface CustomerDebtRepaymentRepository extends JpaRepository<CustomerDebtRepayment, UUID> {
    List<CustomerDebtRepayment> findAllByCustomer_Id(UUID customer_id);
}
