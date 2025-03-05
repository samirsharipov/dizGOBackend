package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.TaskType;

import java.util.List;
import java.util.UUID;

public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {
    List<TaskType> findAllByBranchId(UUID branchId);

    Page<TaskType> findAllByBranch_Id(UUID branch_id, Pageable pageable);
}
