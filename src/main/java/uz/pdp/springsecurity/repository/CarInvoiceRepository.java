package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.CarInvoice;
import uz.pdp.springsecurity.enums.CarInvoiceType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CarInvoiceRepository extends JpaRepository<CarInvoice, UUID> {
    List<CarInvoice> findAllByCarId(UUID car_id);

    Page<CarInvoice> findAllByCarIdOrderByCreatedAtDesc(UUID car_id, Pageable pageable);

    @Query(value = "SELECT * FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type) ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type)",
            nativeQuery = true)
    Page<CarInvoice> findAllByBranchIdAndTypeEqualsIgnoreCaseOrderByCreatedAtDesc(
            @Param("branchId") UUID branchId,
            @Param("type") String type,
            Pageable pageable
    );

    @Query(value = "SELECT SUM(amount) FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type)", nativeQuery = true)
    Double getTotalAmountByBranchIdAndTypeIgnoreCase(
            @Param("branchId") UUID branchId,
            @Param("type") String type
    );
    @Query(value = "SELECT * FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type) AND created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type) AND created_at BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Page<CarInvoice> findAllByBranchIdAndTypeEqualsIgnoreCaseAndCreatedAtBetween(
            @Param("branchId") UUID branchId,
            @Param("type") String type,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    @Query(value = "SELECT SUM(amount) FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type) AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    Double getTotalAmountByBranchIdAndTypeIgnoreCaseAndCreatedAtBetween(
            @Param("branchId") UUID branchId,
            @Param("type") String type,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM car_invoice WHERE branch_id = :branchId AND LOWER(type) = LOWER(:type) AND payment_method_id = :paymentMethodId AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    Double getTotalAmountByBranchIdAndTypeIgnoreCaseAndPaymentMethodIdAndCreatedAtBetween(
            @Param("branchId") UUID branchId,
            @Param("type") String type,
            @Param("paymentMethodId") UUID paymentMethodId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );



}
