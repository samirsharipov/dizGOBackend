package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.OutlayProduct;

import java.util.List;
import java.util.UUID;

public interface OutlayProductRepository extends JpaRepository<OutlayProduct, UUID> {
    List<OutlayProduct> findAllByOutlay_IdAndActiveTrueOrderByCreatedAtDesc(UUID outlay_id);
}
