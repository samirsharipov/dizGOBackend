package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus,UUID> {
    List<TaskStatus> findAllByBranchId(UUID branchId);
    Optional<TaskStatus> findByOrginalName(String name);
    TaskStatus getByOrginalNameAndBranchId(String orginalName, UUID branch_id);
    Optional<TaskStatus> findByBranchIdAndOrginalName(UUID branch_id, String orginalName);
    List<TaskStatus> findAllByBranchIdOrderByRowNumber(UUID branchId);
    Long countByBranchId(UUID branchId);

    List<TaskStatus> findAllByBranchIdOrderByRowNumberAsc(UUID branch_id);

}
