package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.History;
import uz.dizgo.erp.enums.HistoryName;

import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {
    Page<History> findAllByBranch_BusinessIdOrBranchIdOrUserId(UUID businessId, UUID branchId, UUID userID, Pageable pageable);
    Page<History> findAllByBranch_BusinessIdAndName(UUID businessId, HistoryName name, Pageable pageable);
    Page<History> findAllByBranchIdAndName(UUID branchId, HistoryName name, Pageable pageable);
    Page<History> findAllByUserIdAndName(UUID userID, HistoryName name, Pageable pageable);
}
