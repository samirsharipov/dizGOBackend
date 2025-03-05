package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ProjectType;

import java.util.List;
import java.util.UUID;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, UUID> {

    List<ProjectType> findAllByBranchId(UUID branchId);

    Page<ProjectType> findAllByBranch_Id(UUID branch_id, Pageable pageable);
}
