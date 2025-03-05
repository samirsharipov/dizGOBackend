package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.CostType;

import java.util.List;
import java.util.UUID;

public interface CostTypeRepository extends JpaRepository<CostType, UUID> {
    List<CostType> findAllByBranchIdAndDeleteFalseOrderByCreatedAtDesc(UUID branchId);

    Page<CostType> findAllByBranch_idAndDeleteFalseOrderByCreatedAtDesc(UUID branchId, Pageable pageable);
}
