package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.WarehouseFloor;

import java.util.List;
import java.util.UUID;

public interface FloorRepository extends JpaRepository<WarehouseFloor, UUID> {
    List<WarehouseFloor> findAllByBuildingIdAndActiveTrue(UUID building_id);
}
