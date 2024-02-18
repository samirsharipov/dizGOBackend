package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.WarehouseBuilding;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<WarehouseBuilding, UUID> {
    List<WarehouseBuilding> findAllByBranch_IdAndActiveTrue(UUID branch_id);
}
