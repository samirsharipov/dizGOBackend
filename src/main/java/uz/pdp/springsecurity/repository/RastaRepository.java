package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.WarehouseRasta;

import java.util.List;
import java.util.UUID;

public interface RastaRepository extends JpaRepository<WarehouseRasta, UUID> {
    List<WarehouseRasta> findAllBySectorIdAndActiveTrue(UUID sector_id);
}
