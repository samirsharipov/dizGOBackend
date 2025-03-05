package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PurchaseOutlayCategory;

import java.util.List;
import java.util.UUID;

public interface PurchaseOutlayCategoryRepository extends JpaRepository<PurchaseOutlayCategory, UUID> {

    List<PurchaseOutlayCategory> findAllByBusinessIdAndDeletedFalse(UUID businessId);

}
