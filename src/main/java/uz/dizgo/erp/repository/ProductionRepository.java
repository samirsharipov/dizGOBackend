package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.dizgo.erp.entity.Production;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface ProductionRepository extends JpaRepository<Production, UUID> {
    List<Production> findAllByBranchIdAndDoneIsTrueOrderByCreatedAtDesc(UUID branchId);

    Page<Production> findAllByBranchIdAndDoneIsTrue(UUID branchId, Pageable pageable);

    List<Production> findAllByProduct_CategoryIdAndProduct_BrandIdAndProduct_BranchIdAndDoneIsTrue(UUID product_category_id, UUID product_brand_id, UUID product_branch_business_id);

    List<Production> findAllByProduct_CategoryIdAndProduct_BranchIdAndDoneIsTrue(UUID product_category_id, UUID product_branch_business_id);

    List<Production> findAllByProduct_BrandIdAndProduct_BranchIdAndDoneIsTrue(UUID product_category_id, UUID product_branch_business_id);

    Page<Production> findAllByBranchIdAndDoneIsTrueAndProduct_NameContainingIgnoreCase(UUID branch_id, String product_name, Pageable pageable);

    @Query(value = "SELECT SUM(quantity) FROM production WHERE created_at BETWEEN ?1 AND  ?2 AND branch_id = ?3", nativeQuery = true)
    Double amountByCreatedAtBetweenAndBranchId(Timestamp from, Timestamp to, UUID branch_id);

    @Query(value = "SELECT SUM(total_price) FROM production WHERE created_at BETWEEN ?1 AND  ?2 AND branch_id = ?3 AND done = true", nativeQuery = true)
    Double priceByCreatedAtBetweenAndBranchId(Timestamp from, Timestamp to, UUID branch_id);

    @Query(value = "SELECT SUM(quantity) FROM production WHERE product_id = ?1", nativeQuery = true)
    Double quantityByProductSingle(UUID productId);


    @Query(value = "SELECT SUM(quantity) FROM production WHERE product_id = ?1 AND branch_id = ?2", nativeQuery = true)
    Double quantityByProductSingleAndBranchId(UUID productId, UUID branchId);
}
