package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.WorkTimeLate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkTimeLateRepository extends JpaRepository<WorkTimeLate, UUID> {
    Optional<WorkTimeLate> findByUserIdAndBranchId(UUID userID, UUID branchId);

    List<WorkTimeLate> findAllByBranchId(UUID branchId);
}
