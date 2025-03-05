package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.dizgo.erp.entity.DebtCanculs;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface DebtCanculsRepository extends JpaRepository<DebtCanculs, UUID> {
    @Query(nativeQuery = true, value = "SELECT * from debt_canculs dc join trade t on t.id = dc.trade_id where t.branch_id = :branchId")
    List<DebtCanculs> findAllByTradeBranch_Id(UUID branchId);
    @Query(nativeQuery = true, value = "SELECT * from debt_canculs dc join trade t on t.id = dc.trade_id where t.branch_id = :branchId and dc.created_at Between :startDate and :endDate")
    List<DebtCanculs> findAllByTradeBranch_IdAndCreatedAtBetween(UUID branchId, Date startDate, Date endDate);
}