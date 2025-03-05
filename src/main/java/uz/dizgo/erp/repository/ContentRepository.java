package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Content;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {
    Page<Content> findAllByBranch_Id(UUID branch_id, Pageable pageable);

    List<Content> findAllByBranchId(UUID branchId);

    Page<Content> findAllByProduct_NameContainingIgnoreCaseAndBranchId(String product_name, UUID branch_id, Pageable pageable);
}
