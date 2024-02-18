package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.SupplierBalanceHistory;

import java.util.List;
import java.util.UUID;

public interface SupplierBalanceHistoryRepository extends JpaRepository<SupplierBalanceHistory, UUID> {
    List<SupplierBalanceHistory> findAllBySupplierIdOrderByCreatedAtDesc(UUID supplier_id);
}
