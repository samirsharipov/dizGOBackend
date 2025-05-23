package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.CustomerSupplier;

import java.util.Optional;
import java.util.UUID;

public interface CustomerSupplierRepository extends JpaRepository<CustomerSupplier, UUID> {
    boolean existsBySupplierIdOrCustomerId(UUID supplierId, UUID customerId);

    Optional<CustomerSupplier> findByCustomerId(UUID customerId);

    Optional<CustomerSupplier> findBySupplierId(UUID supplierId);

    Page<CustomerSupplier> findAllByCustomer_BusinessId(UUID customer_business_id, Pageable pageable);
}
