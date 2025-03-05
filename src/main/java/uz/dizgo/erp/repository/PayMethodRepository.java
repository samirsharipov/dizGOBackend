package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PaymentMethod;

import java.util.Optional;
import java.util.UUID;

public interface PayMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    PaymentMethod findByTypeContainingIgnoreCaseOrderByCreatedAtDesc(String type);

    Optional<PaymentMethod> findByType(String type);

}
