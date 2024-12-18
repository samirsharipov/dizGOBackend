package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.PaymentMethod;

import java.util.Optional;
import java.util.UUID;

public interface PayMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    boolean existsByType(String type);

    PaymentMethod findByTypeContainingIgnoreCaseOrderByCreatedAtDesc(String type);

    Optional<PaymentMethod> findByType(String type);

}
