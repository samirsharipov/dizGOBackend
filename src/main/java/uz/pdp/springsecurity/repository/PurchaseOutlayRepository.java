package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.PurchaseOutlay;

import java.util.List;
import java.util.UUID;

public interface PurchaseOutlayRepository extends JpaRepository<PurchaseOutlay, UUID> {
    List<PurchaseOutlay> findAllByPurchaseId(UUID purchaseId);
}
