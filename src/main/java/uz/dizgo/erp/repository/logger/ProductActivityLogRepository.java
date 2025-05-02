package uz.dizgo.erp.repository.logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ProductActivityLog;
import uz.dizgo.erp.entity.template.ProductActionType;


import java.util.UUID;

public interface ProductActivityLogRepository extends JpaRepository<ProductActivityLog, UUID> {

    Page<ProductActivityLog> findAllByProductId(UUID productId, Pageable pageable);

    Page<ProductActivityLog> findAllByProductIdAndActionType(UUID productId, ProductActionType actionType, Pageable pageable);
}
