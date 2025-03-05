package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ProductHistory;

import java.util.UUID;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, UUID> {
}
