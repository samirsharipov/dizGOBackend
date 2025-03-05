package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.Stage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StageRepository extends JpaRepository<Stage, UUID> {

    List<Stage> findAllByBranchId(UUID branch_id);
    Page<Stage> findAllByBranch_Id(UUID branchId, Pageable pageable);


    Optional<Stage> findByNameAndBranch(String stageName, Branch branch);
}
