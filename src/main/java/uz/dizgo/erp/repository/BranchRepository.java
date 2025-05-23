package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Branch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
    List<Branch> findAllByBusiness_Id(UUID business_id);

    int countAllByBusiness_Id(UUID business_id);

    Optional<Branch> findByBranchCategory_Id(UUID branchCategoryId);

    boolean existsByBranchCategory_Id(UUID branchCategoryId);
}
