package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.CustomerDebt;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerDebtRepository extends JpaRepository<CustomerDebt, UUID> {

    @Query(value = "select sum (c.debtSum) from CustomerDebt c where c.customer.business.id = :businessId AND c.createdAt >= :startDate AND c.createdAt <= :endDate and (c.delete = false or c.delete = null)")
    Double totalCustomerDebtSumByBusiness(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "select sum (c.debtSum) from CustomerDebt c where c.customer.branch.id = :branchId AND c.createdAt >= :startDate AND c.createdAt <= :endDate and (c.delete = false or c.delete = null)")
    Double totalCustomerDebtSum(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    Optional<CustomerDebt> findByTrade_Id(UUID trade_id);
    List<CustomerDebt> findByCustomer_Id(UUID customerId);

    void deleteAllByTradeId(UUID tradeId);
}
