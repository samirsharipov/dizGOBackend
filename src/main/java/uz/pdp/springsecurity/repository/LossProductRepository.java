package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.LossProduct;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface LossProductRepository extends JpaRepository<LossProduct, UUID> {

    List<LossProduct> findAllByLoss_Branch_IdAndLoss_Id(UUID branch_id, UUID loss_id);
    Page<LossProduct> findAllByLoss_Branch_IdOrderByCreatedAtDesc(UUID loss_branch_id, Pageable pageable);
    Page<LossProduct> findAllByLoss_Branch_IdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID loss_branch_id, Date startDate, Date endDate, Pageable pageable);




    @Query("SELECT SUM(lp.quantity * lp.product.buyPrice) " +
           "FROM LossProduct lp " +
           "WHERE lp.loss.branch.id = :branchId " +
           "AND lp.status = :status " +
           "AND lp.createdAt BETWEEN :startDate AND :endDate")
    Double calculateTotalCostByStatusAndDateRangeAndProductBuyPrice(
            @Param("branchId") UUID branchId,
            @Param("status") String status,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    @Query("SELECT SUM(lp.quantity * lp.product.salePrice) " +
           "FROM LossProduct lp " +
           "WHERE lp.loss.branch.id = :branchId " +
           "AND lp.status = :status " +
           "AND lp.createdAt BETWEEN :startDate AND :endDate")
    Double calculateTotalCostByStatusAndDateRangeAndProductSalePrice(
            @Param("branchId") UUID branchId,
            @Param("status") String status,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
