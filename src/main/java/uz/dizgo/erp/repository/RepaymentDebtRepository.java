package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.RepaymentDebt;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RepaymentDebtRepository extends JpaRepository<RepaymentDebt, UUID> {
    @Query("select sum(r.debtSum) from RepaymentDebt r where r.customer.branch.id= :branchId and r.paymentMethod.type = :paymentMethodName and r.createdAt >= :startDate AND r.createdAt <= :endDate and r.delete = false and r.payDate = null")
    Double getTotalSum(@Param("branchId") UUID branchId, @Param("paymentMethodName") String paymentMethodName, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select sum(r.debtSum) from RepaymentDebt r where r.customer.branch.id= :branchId and r.paymentMethod.type = :paymentMethodName and r.payDate >= :startDate AND r.payDate <= :endDate and r.delete = false ")
    Double getTotalSumWithPayDate(@Param("branchId") UUID branchId, @Param("paymentMethodName") String paymentMethodName, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select sum(r.debtSum) from RepaymentDebt r where r.customer.branch.business.id= :businessId and  r.paymentMethod.type = :paymentMethodName and r.createdAt >= :startDate AND r.createdAt <= :endDate and r.delete = false and r.payDate = null ")
    Double getTotalSumByBusiness(@Param("businessId") UUID branchId, @Param("paymentMethodName") String paymentMethodName, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select sum(r.debtSum) from RepaymentDebt r where r.customer.branch.business.id= :businessId and  r.paymentMethod.type = :paymentMethodName and r.payDate >= :startDate AND r.payDate <= :endDate and r.delete = false ")
    Double getTotalSumByBusinessWithPayDate(@Param("businessId") UUID branchId, @Param("paymentMethodName") String paymentMethodName, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    List<RepaymentDebt> findAllByCustomer_Id(UUID customer_id);

    @Query(nativeQuery = true, value = "SELECT * FROM repayment_debt rd join customer c on c.id = rd.customer_id where rd.is_dollar_repayment = :isDollar and c.branch_id = :branchId and rd.created_at between :startDate and :endDate and rd.payment_method_id = :payMethodId")
    List<RepaymentDebt> findAllByIsDollarAndBranchIdAndStartDateAndEndDate(Boolean isDollar, UUID branchId, Date startDate, Date endDate, UUID payMethodId);

    @Query(nativeQuery = true, value = "SELECT sum(debt_sum) FROM repayment_debt rd join customer c on c.id = rd.customer_id where rd.payment_method_id = :payMethodId and c.branch_id = :branchId")
    Double findAllDebtAmountByPayMethodIdAndBranchId(UUID payMethodId, UUID branchId);

    @Query(nativeQuery = true, value = "SELECT sum(debt_sum) FROM repayment_debt rd join customer c on c.id = rd.customer_id where rd.payment_method_id = :payMethodId and c.branch_id = :branchId and rd.created_at between :startDate and :endDate")
    Double findAllDebtAmountByPayMethodIdAndBranchIdAndDate(UUID payMethodId, UUID branchId, Date startDate, Date endDate);
}