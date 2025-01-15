package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Purchase;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    Optional<Purchase> findFirstByBranchIdOrderByCreatedAtDesc(UUID branchId);

    Purchase findByBranchIdAndInvoiceContainingIgnoreCase(UUID branch_id, String invoice);

    List<Purchase> findAllByBranch_IdOrderByCreatedAtDesc(UUID branch_id);

    @Query("SELECT s.name, s.phoneNumber, SUM(p.totalSum) FROM Purchase p JOIN p.supplier s WHERE p.branch.id = :branchId GROUP BY s.name, s.phoneNumber ORDER BY SUM(p.totalSum) DESC")
    List<Object[]> findTop10SuppliersByPurchase(@Param("branchId") UUID branchId);

    @Query("SELECT s.name, s.phoneNumber, SUM(p.totalSum) FROM Purchase p JOIN p.supplier s WHERE p.branch.id = :branchId AND p.date >= :startDate AND p.date <= :endDate GROUP BY s.name, s.phoneNumber ORDER BY SUM(p.totalSum) DESC")
    List<Object[]> findTop10SuppliersByPurchaseAndDate(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    List<Purchase> findAllByCreatedAtBetweenAndBranchId(Timestamp start, Timestamp end, UUID branch_id);

    List<Purchase> findAllByCreatedAtBetweenAndBranch_BusinessId(Timestamp start, Timestamp end, UUID businessId);

    List<Purchase> findAllBySupplierId(UUID dealer_id);

    List<Purchase> findAllByBranch_BusinessIdOrderByCreatedAtDesc(UUID businessId);

    @Query(value = "select sum (p.totalSum) from Purchase p where p.branch.id = :branchId and p.date >= :startDate and p.date <= :endDate")
    Double totalPurchase(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "select sum (p.debtSum) from Purchase p where p.branch.id = :branchId and p.date >= :startDate and p.date <= :endDate")
    Double totalMyDebt(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    Page<Purchase> findAllByBranchIdAndSellerIdAndSupplierIdAndDate(UUID branchId, UUID userId, UUID supplierId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSellerIdAndSupplierIdAndDate(UUID businessID, UUID userId, UUID supplierId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndSellerIdAndSupplierId(UUID branchId, UUID userId, UUID supplierId, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSellerIdAndSupplierId(UUID businessID, UUID userId, UUID supplierId, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndSellerIdAndDate(UUID branchId, UUID userId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSellerIdAndDate(UUID businessID, UUID userId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndSupplierIdAndDate(UUID branchId, UUID supplierId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSupplierIdAndDate(UUID businessID, UUID supplierId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndSellerId(UUID branchId, UUID userId, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSellerId(UUID businessID, UUID userId, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndSupplierId(UUID branchId, UUID supplierId, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndSupplierId(UUID businessID, UUID supplierId, Pageable pageable);

    Page<Purchase> findAllByBranchIdAndDate(UUID branchId, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessIdAndDate(UUID businessID, Date paymentStatus, Pageable pageable);

    Page<Purchase> findAllByBranchId(UUID branchId, Pageable pageable);

    Page<Purchase> findAllByBranch_BusinessId(UUID businessID, Pageable pageable);

    @Query(value = "SELECT COUNT(p) FROM Purchase p WHERE p.branch.id = :branchId AND p.date >= :startDate AND p.date <= :endDate")
    Long countPurchasesByBranchIdAndDateBetween(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT COUNT(p) FROM Purchase p WHERE p.branch.business.id = :businessId AND p.date >= :startDate AND p.date <= :endDate")
    Long countPurchasesByBusinessIdAndDateBetween(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
