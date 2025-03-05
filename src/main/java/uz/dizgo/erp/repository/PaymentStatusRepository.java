package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PaymentStatus;

import java.util.UUID;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus , UUID> {

    boolean existsByStatus(String status);

    PaymentStatus findByStatus(String statusName);
}
