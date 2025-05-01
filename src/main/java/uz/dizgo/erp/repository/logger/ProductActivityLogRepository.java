package uz.dizgo.erp.repository.logger;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ProductActivityLog;

import java.util.UUID;

public interface ProductActivityLogRepository extends JpaRepository<ProductActivityLog, UUID> {
}
