package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.LostProduction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LostProductionRepository extends JpaRepository<LostProduction, UUID> {
    Optional<LostProduction> findByTaskStatusId(UUID taskStatusId);
    List<LostProduction> findByTaskStatus_BranchId(UUID branchId);
}
