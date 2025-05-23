package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.dizgo.erp.entity.PurchaseProduct;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, UUID> {
    List<PurchaseProduct> findAllByPurchaseId(UUID purchaseId);

    List<PurchaseProduct> findAllByPurchase_BranchId(UUID branchId);

    List<PurchaseProduct> findAllByPurchase_BranchIdAndPurchase_SupplierId(UUID purchase_branch_id, UUID purchase_supplier_id);

    List<PurchaseProduct> findAllByCreatedAtBetweenAndPurchase_BranchId(Timestamp startDate, Timestamp endDate, UUID purchase_branch_id);

    List<PurchaseProduct> findAllByCreatedAtBetweenAndProduct_BranchIdAndPurchase_SupplierId(Timestamp start, Timestamp end, UUID branchId, UUID supplierId);

    Page<PurchaseProduct> findAllByPurchase_BranchIdAndProductIdOrderByCreatedAtDesc(UUID branch_id, UUID productId, Pageable pageable);

    @Query(value = "SELECT SUM(purchased_quantity) FROM purchase_product WHERE product_id = ?1", nativeQuery = true)
    Double quantityByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(purchased_quantity) FROM purchase_product WHERE product_id = ?1 AND purchase_id IN (SELECT id FROM purchase WHERE branch_id = ?2)", nativeQuery = true)
    Double quantityByProductSingleAndBranchId(UUID productId, UUID branchId);
}
