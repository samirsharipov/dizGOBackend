package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Outlay;
import uz.dizgo.erp.enums.OUTLAY_STATUS;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OutlayRepository extends JpaRepository<Outlay, UUID> {

    @Query(value = "SELECT * FROM outlay o WHERE DATE(o.date) = ?1 and o.branch_id = ?2", nativeQuery = true)
    List<Outlay> findAllByDate(java.sql.Date date, UUID branch_id);

    @Query(value = "select * from outlay o inner join branches b on o.branch_id = b.id where b.business_id = ?1 and o.date = ?2", nativeQuery = true)
    List<Outlay> findAllByDateAndBusinessId(UUID business_id, java.sql.Date date);

    List<Outlay> findAllByBranch_IdOrderByCreatedAtDesc(UUID branch_id);

    Page<Outlay> findByBranch_IdAndStatusEqualsOrderByCreatedAtDesc(UUID branch_id, OUTLAY_STATUS status, Pageable pageable);

    Page<Outlay> findByBranch_IdAndStatusEqualsAndCreatedAtBetweenOrderByCreatedAtDesc(UUID branch_id, OUTLAY_STATUS status, Pageable pageable,Date startDate,Date endDAte);

    List<Outlay> findAllByBranch_BusinessIdOrderByCreatedAtDesc(UUID branch_id);

    List<Outlay> findAllByCreatedAtBetweenAndBranchId(Timestamp startDate, Timestamp endDate, UUID branch_id);

    List<Outlay> findAllByCreatedAtBetweenAndBranch_BusinessId(Timestamp startDate, Timestamp endDate, UUID businessId);

    List<Outlay> findAllByBranch_IdAndOutlayCategoryId(UUID branch_id, UUID categoryId);

    List<Outlay> findAllByCreatedAtBetweenAndBranchIdAndOutlayCategoryId(Timestamp from, Timestamp to, UUID branch_id, UUID outlayCategory_id);

    @Query(value = "select * from outlay inner join branches b on b.business_id = ?1", nativeQuery = true)
    List<Outlay> findAllByBusinessId(UUID businessId);

    @Query(value = "SELECT * FROM outlay WHERE branch_id IN (SELECT id FROM branches WHERE business_id = ?1) AND payment_method_id = ?2", nativeQuery = true)
    List<Outlay> findAllByBusinessIdAndPaymentMethodId(UUID businessId, UUID paymentMethodId);

    List<Outlay> findAllByBranch_IdAndPaymentMethod_IdAndDollarOutlayIsFalse(UUID branch_id, UUID paymentMethod_id);

    List<Outlay> findAllByBranch_IdAndPaymentMethod_IdAndDollarOutlayIsTrue(UUID branch_id, UUID paymentMethod_id);

    @Query(value = "SELECT * FROM outlay WHERE branch_id = :branchId AND payment_method_id = :paymentMethodId AND date BETWEEN :startDate AND :endDate and dollar_outlay = false", nativeQuery = true)
    List<Outlay> findAllByBranchIdAndPaymentMethodIdAndDateBetween(
            @Param("branchId") UUID branchId,
            @Param("paymentMethodId") UUID paymentMethodId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    @Query(value = "SELECT * FROM outlay WHERE branch_id = :branchId AND payment_method_id = :paymentMethodId AND date BETWEEN :startDate AND :endDate AND dollar_outlay = :isDollarOutlay AND status = :status", nativeQuery = true)
    List<Outlay> findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndStatus(
            @Param("branchId") UUID branchId,
            @Param("paymentMethodId") UUID paymentMethodId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("isDollarOutlay") boolean isDollarOutlay,
            @Param("status") String status
    );

    @Query(value = "SELECT * FROM outlay WHERE branch_id = :branchId AND payment_method_id = :paymentMethodId AND date BETWEEN :startDate AND :endDate and dollar_outlay = true", nativeQuery = true)
    List<Outlay> findAllByBranchIdAndPaymentMethodIdAndDateBetweenAndTrue(
            @Param("branchId") UUID branchId,
            @Param("paymentMethodId") UUID paymentMethodId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    @Query(value = "SELECT SUM(total_sum) FROM outlay WHERE created_at BETWEEN ?1 AND  ?2 AND branch_id = ?3", nativeQuery = true)
    Double outlayByCreatedAtBetweenAndBranchId(Timestamp from, Timestamp to, UUID branchId);

    @Query(value = "SELECT SUM(o.totalSum) FROM Outlay o WHERE o.branch.business.id = :businessId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Double outlayByCreatedAtBetweenAndBusinessId(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT SUM(o.totalSum) FROM Outlay o WHERE o.branch.id = :branchId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Double outlayByBranchIdAndCreatedAtBetween(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT SUM(o.totalSum) FROM Outlay o WHERE o.branch.business.id = :businessId AND o.paymentMethod.id = :paymethodId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Double outlayByCreatedAtBetweenAndBusinessIdAndPaymentMethod(@Param("businessId") UUID businessId, @Param("paymethodId") UUID paymethodId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT SUM(o.totalSum) FROM Outlay o WHERE o.branch.id = :branchId AND o.paymentMethod.id = :paymethodId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    Double outlayByCreatedAtBetweenAndPaymentMethodByBranch(@Param("branchId") UUID businessId, @Param("paymethodId") UUID paymethodId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
