package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Resource;

import java.util.List;
import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    List<Resource> findAllByBranchIdAndActiveTrue(UUID branchId);

    List<Resource> findAllByBranch_BusinessIdAndActiveTrue(UUID branch_business_id);
}
