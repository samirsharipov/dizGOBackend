package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Warehouse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    Optional<Warehouse> findByBranchIdAndProductId(UUID branchId, UUID productId);

    List<Warehouse> findAllByBranch_BusinessIdAndProductId(UUID branchId, UUID productId);

    List<Warehouse> findAllByBranch_Business_Id(UUID branch_business_id);

    Warehouse findAllByProductTypePrice_BarcodeAndBranch_BusinessId(String productTypePrice_barcode, UUID branch_business_id);

    Page<Warehouse> findAllByBranch_BusinessIdAndAmountNotOrderByAmountAsc(UUID product_business_id, double amount, Pageable pageable);

    Page<Warehouse> findAllByBranchIdAndAmountNotOrderByAmountAsc(UUID branch_id, double amount, Pageable pageable);

    Page<Warehouse> findAllByBranchIdAndAmountIsNotOrderByLastSoldDate(UUID branch_id, double amount, Pageable pageable);

    Optional<Warehouse> findByBranchIdAndProductTypePriceId(UUID branchId, UUID productTypePriceId);

    Optional<Warehouse> findByProductIdAndBranchId(UUID product_id, UUID branch_id);

    Optional<Warehouse> findByProductTypePriceIdAndBranchId(UUID productTypePrice_id, UUID branch_id);

    Optional<Warehouse> findByProduct_Id(UUID productId);

    List<Warehouse> findAllByProduct_Id(UUID productId);

    List<Warehouse> findAllByProduct_IdAndBranch_Id(UUID product_id, UUID branch_id);

    List<Warehouse> findAllByBranchId(UUID branchId);
    Page<Warehouse> findAllByBranch_Id(UUID branchId, Pageable pageable);
    Page<Warehouse> findAllByBranch_IdAndProduct_ActiveTrue(UUID branchId, Pageable pageable);

    @Query(value = "SELECT SUM(w.amount * w.product.buyPrice) FROM Warehouse w WHERE  w.product.id = :productId and w.createdAt >= :startDate AND w.createdAt <= :endDate")
    Double totalSumProduct(@Param("productId") UUID productId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT SUM(w.amount * w.productTypePrice.buyPrice) FROM Warehouse w WHERE  w.productTypePrice.id = :productTypePriceId and w.createdAt >= :startDate AND w.createdAt <= :endDate")
    Double totalSumProductTypePrice(@Param("productTypePriceId") UUID productTypePriceId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    void deleteAllByProductId(UUID productId);
    void deleteAllByProductTypePrice_ProductId(UUID productId);

    @Query(value = "SELECT SUM(amount) FROM warehouse WHERE product_type_price_id = ?1", nativeQuery = true)
    Double amountByProductTypePrice(UUID productTypePriceId);
    @Query(value = "SELECT SUM(amount) FROM warehouse WHERE product_type_price_id = ?1 AND branch_id = ?2", nativeQuery = true)
    Double amountByProductTypePriceAndBranchId(UUID productTypePriceId, UUID branchId);

    @Query(value = "SELECT SUM(w.amount) FROM Warehouse w WHERE w.product.id = ?1")
    Double amountByProductSingle(UUID productId);
    @Query(value = "SELECT SUM(w.amount) FROM Warehouse w WHERE w.productTypePrice.product.id = ?1")
    Double amountByProductMany(UUID productId);

    @Query(value = "SELECT w.amount FROM Warehouse w WHERE w.product.id = ?1 AND w.branch.id = ?2")
    Double amountByProductSingleAndBranchId(UUID productId, UUID branchId);
    @Query(value = "SELECT SUM(w.amount) FROM Warehouse w WHERE w.productTypePrice.product.id = ?1 AND w.branch.id = ?2")
    Double amountByProductManyAndBranchId(UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(w.amount * w.product.salePrice) FROM Warehouse w WHERE w.product.id = ?1")
    Double salePriceByProductSingle(UUID productId);
    @Query(value = "SELECT SUM(w.amount * w.productTypePrice.salePrice) FROM Warehouse w WHERE w.productTypePrice.id IN (SELECT p.id FROM ProductTypePrice p WHERE p.product.id = ?1)")
    Double salePriceByProductMany(UUID productId);

    @Query(value = "SELECT SUM(w.amount * w.product.salePrice) FROM Warehouse w WHERE w.product.id = ?1 AND w.branch.id = ?2")
    Double salePriceByProductSingleAndBranchId(UUID productId, UUID branchId);
    @Query(value = "SELECT SUM(w.amount * w.productTypePrice.salePrice) FROM Warehouse w WHERE w.productTypePrice.id IN (SELECT p.id FROM ProductTypePrice p WHERE p.product.id = ?1) AND w.branch.id = ?2")
    Double salePriceByProductManyAndBranchId(UUID productId, UUID branchId);
}
