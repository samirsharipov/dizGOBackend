package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Loss;

import java.util.Date;
import java.util.UUID;

public interface LossRepository extends JpaRepository<Loss, UUID> {
    Page<Loss> findAllByBranchIdOrderByCreatedAtDesc(UUID branchId, Pageable pageable);
    Page<Loss> findAllByBranchIdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID branchId, Date startDate, Date endDate, Pageable pageable);
}
