package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.PaymentStatus;
import uz.pdp.springsecurity.enums.StatusName;

import java.util.UUID;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus , UUID> {

    boolean existsByStatus(String status);

    PaymentStatus findByStatus(String statusName);
}
