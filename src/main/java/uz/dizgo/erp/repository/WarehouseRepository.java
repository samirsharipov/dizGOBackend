package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Warehouse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    Optional<Warehouse> findByBranchIdAndProductId(UUID branchId, UUID productId);

    List<Warehouse> findAllByBranch_BusinessIdAndProductId(UUID branchId, UUID productId);

    List<Warehouse> findAllByBranch_Business_Id(UUID branch_business_id);

    Page<Warehouse> findAllByBranch_BusinessIdAndAmountNotOrderByAmountAsc(UUID product_business_id, double amount, Pageable pageable);

    Page<Warehouse> findAllByBranchIdAndAmountNotOrderByAmountAsc(UUID branch_id, double amount, Pageable pageable);

    Page<Warehouse> findAllByBranchIdAndAmountIsNotOrderByLastSoldDate(UUID branch_id, double amount, Pageable pageable);

    Optional<Warehouse> findByProductIdAndBranchId(UUID product_id, UUID branch_id);

    List<Warehouse> findAllByProduct_Id(UUID productId);

    Optional<Warehouse> findByProduct_IdAndBranchId(UUID productId, UUID branch_id);

    List<Warehouse> findAllByBranchId(UUID branchId);

    Page<Warehouse> findAllByBranch_Id(UUID branchId, Pageable pageable);

    Page<Warehouse> findAllByBranch_IdAndProduct_ActiveTrue(UUID branchId, Pageable pageable);

    @Query(value = "SELECT SUM(w.amount * w.product.buyPrice) FROM Warehouse w WHERE  w.product.id = :productId and w.createdAt >= :startDate AND w.createdAt <= :endDate")
    Double totalSumProduct(@Param("productId") UUID productId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    void deleteAllByProductId(UUID productId);

    @Query(value = "SELECT SUM(w.amount) FROM Warehouse w WHERE w.product.id = ?1")
    Double amountByProductSingle(UUID productId);

    @Query(value = "SELECT w.amount FROM Warehouse w WHERE w.product.id = ?1 AND w.branch.id = ?2")
    Double amountByProductSingleAndBranchId(UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(w.amount * w.product.salePrice) FROM Warehouse w WHERE w.product.id = ?1")
    Double salePriceByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(w.amount * w.product.salePrice) FROM Warehouse w WHERE w.product.id = ?1 AND w.branch.id = ?2")
    Double salePriceByProductSingleAndBranchId(UUID productId, UUID branchId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.branch.id = :branchId " +
            "and w.amount > 0")
    Long countProductsWithAmountGreaterThanZeroByBranch(@Param("branchId") UUID branchId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.product.business.id = :businessId " +
            "and w.amount > 0")
    Long countProductsWithAmountGreaterThanZeroByBusiness(@Param("businessId") UUID businessId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.branch.id = :branchId " +
            "and w.amount <= 0")
    Long countProductsWithAmountLessThanOrEqualToZeroByBranch(@Param("branchId") UUID branchId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.product.business.id = :businessId " +
            "and w.amount <= 0")
    Long countProductsWithAmountLessThanOrEqualToZeroByBusiness(@Param("businessId") UUID businessId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.branch.id = :branchId " +
            "and w.amount <= w.product.minQuantity")
    Long countProductsWithAmountLessThanOrEqualToMinQuantityByBranch(@Param("branchId") UUID branchId);

    @Query("select count(w.id) from Warehouse w " +
            "where w.product.business.id = :businessId " +
            "and w.amount <= w.product.minQuantity")
    Long countProductsWithAmountLessThanOrEqualToMinQuantityByBusiness(@Param("businessId") UUID businessId);

    List<Warehouse> findAllByBranchIdAndProductIdIn(UUID id, Set<UUID> productIds);
}
