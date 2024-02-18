package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.WarehouseSector;

import java.util.List;
import java.util.UUID;

public interface SectorRepository extends JpaRepository<WarehouseSector, UUID> {
    List<WarehouseSector> findAllByFloorIdAndActiveTrue(UUID floor_id);
}
