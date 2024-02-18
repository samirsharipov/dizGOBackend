package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.WarehouseFloor;

import java.util.List;
import java.util.UUID;

public interface FloorRepository extends JpaRepository<WarehouseFloor, UUID> {
    List<WarehouseFloor> findAllByBuildingIdAndActiveTrue(UUID building_id);
}
