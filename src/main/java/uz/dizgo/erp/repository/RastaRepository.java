package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.WarehouseRasta;

import java.util.List;
import java.util.UUID;

public interface RastaRepository extends JpaRepository<WarehouseRasta, UUID> {
    List<WarehouseRasta> findAllBySectorIdAndActiveTrue(UUID sector_id);
}
