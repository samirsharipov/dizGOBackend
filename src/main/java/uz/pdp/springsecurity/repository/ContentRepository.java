package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Content;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {
    Page<Content> findAllByBranch_Id(UUID branch_id, Pageable pageable);

    List<Content> findAllByBranchId(UUID branchId);


    Page<Content> findAllByProduct_NameContainingIgnoreCaseAndBranchIdOrProductTypePrice_NameContainingIgnoreCaseAndBranchId(String product_name, UUID branch_id, String productTypePrice_name, UUID branch_id2, Pageable pageable);

    Page<Content> findAllByProductTypePrice_NameContainingIgnoreCaseAndBranchId(String product_name, UUID branch_id, Pageable pageable);
}
