package uz.pdp.springsecurity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Trade;
import uz.pdp.springsecurity.payload.projections.DataProjection;
import uz.pdp.springsecurity.payload.projections.MonthProjection;
import uz.pdp.springsecurity.payload.projections.ReportForProduct;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID>, JpaSpecificationExecutor<Trade> {
    List<Trade> findAllByTrader_Id(UUID trader_id);

    @Query(nativeQuery = true, value = "SELECT Distinct p.name AS productname,\n" +
                                       "                b.name as branchName,\n" +
                                       "                t.created_at as createddate,\n" +
                                       "                SUM(tp.traded_quantity) AS treaderquantity,\n" +
                                       "                SUM(t.kpi) AS kpi \n" +
                                       "FROM trade t\n" +
                                       "         JOIN users u ON t.trader_id = u.id\n" +
                                       "         JOIN trade_product tp ON tp.trade_id = t.id\n" +
                                       "         JOIN product p ON tp.product_id = p.id\n" +
                                       "        JOIN branches b on t.branch_id = b.id\n" +
                                       "WHERE u.id = :userId\n" +
                                       "GROUP BY p.name, b.id, t.id ")
    List<DataProjection> findAllByuserId(UUID userId);


    @Query(nativeQuery = true, value = "SELECT Distinct ptp.name AS productname,\n" +
                                       "                b.name as branchName,\n" +
                                       "                t.created_at as createddate,\n" +
                                       "                SUM(tp.traded_quantity) AS treaderquantity,\n" +
                                       "                SUM(t.kpi) AS kpi \n" +
                                       "FROM trade_product tp\n" +
                                       "         JOIN product_type_price ptp ON tp.product_type_price_id = ptp.id\n" +
                                       "         JOIN Product p ON ptp.product_id = p.id\n" +
                                       "         JOIN Trade t ON tp.trade_id = t.id\n" +
                                       "         JOIN branches b on t.branch_id = b.id\n" +
                                       "WHERE t.trader_id = :userId\n" +
                                       "GROUP BY ptp.name, b.id, t.id ")
    List<DataProjection> findAllTradeTypeManyProductByUserId(UUID userId);


    @Query(nativeQuery = true, value = "SELECT Distinct p.name AS productname,\n" +
                                       "                b.name as branchName,\n" +
                                       "                t.created_at as createddate,\n" +
                                       "                SUM(tp.traded_quantity) AS treaderquantity,\n" +
                                       "                SUM(t.kpi) AS kpi \n" +
                                       "FROM trade t\n" +
                                       "         JOIN users u ON t.trader_id = u.id\n" +
                                       "         JOIN trade_product tp ON tp.trade_id = t.id\n" +
                                       "         JOIN product p ON tp.product_id = p.id\n" +
                                       "        JOIN branches b on t.branch_id = b.id\n" +
                                       "WHERE u.id = :userId and p.id = :productId\n" +
                                       "      AND t.created_at BETWEEN :startDate AND :endDate\n" +
                                       "GROUP BY p.name, b.id, t.id ")
    List<DataProjection> findAllByUserIdAndProductIdAndDateRange(UUID userId, UUID productId, Date startDate, Date endDate);



    @Query(nativeQuery = true, value = "SELECT Distinct p.name AS productname,\n" +
                                       "                b.name as branchName,\n" +
                                       "                t.created_at as createddate,\n" +
                                       "                SUM(tp.traded_quantity) AS treaderquantity,\n" +
                                       "                SUM(t.kpi) AS kpi \n" +
                                       "FROM trade t\n" +
                                       "         JOIN users u ON t.trader_id = u.id\n" +
                                       "         JOIN trade_product tp ON tp.trade_id = t.id\n" +
                                       "         JOIN product p ON tp.product_id = p.id\n" +
                                       "        JOIN branches b on t.branch_id = b.id\n" +
                                       "WHERE u.id = :userId and t.created_at BETWEEN :start_time AND :end_time \n" +
                                       "GROUP BY p.name, b.id, t.id ")
    List<DataProjection> findAllByUserIdAndSearch(UUID userId, Date start_time, Date end_time);

    @Query(nativeQuery = true, value = "SELECT Distinct ptp.name AS productname,\n" +
                                       "                b.name as branchName,\n" +
                                       "                t.created_at as createddate,\n" +
                                       "                SUM(tp.traded_quantity) AS treaderquantity,\n" +
                                       "                SUM(t.kpi) AS kpi \n" +
                                       "FROM trade_product tp\n" +
                                       "         JOIN product_type_price ptp ON tp.product_type_price_id = ptp.id\n" +
                                       "         JOIN Product p ON ptp.product_id = p.id\n" +
                                       "         JOIN Trade t ON tp.trade_id = t.id\n" +
                                       "         JOIN branches b on t.branch_id = b.id\n" +
                                       "WHERE t.trader_id = :userId and t.created_at BETWEEN :start_time AND :end_time \n" +
                                       "GROUP BY ptp.name, b.id, t.id ")
    List<DataProjection> findAllTradeTypeManyAndSearch(UUID userId, Date start_time, Date end_time);

    @Query(nativeQuery = true, value = "SELECT p.name as productname,\n" +
                                       "                   b.name as branchname,\n" +
                                       "                   tp.traded_quantity as quantity,\n" +
                                       "                   c.name as categoryname,\n" +
                                       "                   t.kpi as kpi,\n" +
                                       "                   tp.created_at as createddate,\n" +
                                       "                   tp.total_sale_price as tsprice FROM trade_product tp\n" +
                                       "                JOIN trade t on t.id = tp.trade_id\n" +
                                       "                JOIN product p on p.id = tp.product_id\n" +
                                       "                JOIN branches b on t.branch_id = b.id\n" +
                                       "                LEFT JOIN category c on p.category_id = c.id\n" +
                                       "            where t.branch_id = :branchId")
    List<ReportForProduct> getAllProductByBranchId(UUID branchId);

    @Query(nativeQuery = true, value = "SELECT ptp.name as productname,\n" +
                                       "                   b.name as branchname,\n" +
                                       "                   tp.traded_quantity as quantity,\n" +
                                       "                   c.name as categoryname,\n" +
                                       "                   t.kpi as kpi,\n" +
                                       "                   tp.created_at as createddate,\n" +
                                       "                   tp.total_sale_price as tsprice FROM trade_product tp\n" +
                                       "                JOIN trade t on t.id = tp.trade_id\n" +
                                       "                JOIN product_type_price ptp on ptp.id = tp.product_type_price_id\n" +
                                       "                JOIN branches b on t.branch_id = b.id\n" +
                                       "                JOIN product p on ptp.product_id = p.id\n" +
                                       "                LEFT JOIN category c on p.category_id = c.id\n" +
                                       "            where t.branch_id = :branchId")
    List<ReportForProduct> getAllProductTypeManyByBranchId(UUID branchId);

    @Query(nativeQuery = true, value = "SELECT p.name as productname,\n" +
                                       "                   b.name as branchname,\n" +
                                       "                   tp.traded_quantity as quantity,\n" +
                                       "                   c.name as categoryname,\n" +
                                       "                   t.kpi as kpi,\n" +
                                       "                   tp.created_at as createddate,\n" +
                                       "                   tp.total_sale_price as tsprice FROM trade_product tp\n" +
                                       "                JOIN trade t on t.id = tp.trade_id\n" +
                                       "                JOIN product p on p.id = tp.product_id\n" +
                                       "                JOIN branches b on t.branch_id = b.id\n" +
                                       "                LEFT JOIN category c on p.category_id = c.id\n" +
                                       "            where t.branch_id = :branchId and t.created_at BETWEEN :start_time AND :end_time")
    List<ReportForProduct> getAllProductByBranchIdAndStartDateAndEndDate(UUID branchId, Timestamp start_time, Timestamp end_time);

    @Query(nativeQuery = true, value = "SELECT ptp.name as productname,\n" +
                                       "                   b.name as branchname,\n" +
                                       "                   tp.traded_quantity as quantity,\n" +
                                       "                   c.name as categoryname,\n" +
                                       "                   t.kpi as kpi,\n" +
                                       "                   tp.created_at as createddate,\n" +
                                       "                   tp.total_sale_price as tsprice FROM trade_product tp\n" +
                                       "                JOIN trade t on t.id = tp.trade_id\n" +
                                       "                JOIN product_type_price ptp on ptp.id = tp.product_type_price_id\n" +
                                       "                JOIN branches b on t.branch_id = b.id\n" +
                                       "                JOIN product p on ptp.product_id = p.id\n" +
                                       "                LEFT JOIN category c on p.category_id = c.id\n" +
                                       "            where t.branch_id = :branchId and t.created_at BETWEEN :start_time AND :end_time")
    List<ReportForProduct> getAllProductTypeManyByBranchIdAndStartDateAndEndDate(UUID branchId, Timestamp start_time, Timestamp end_time);

    Page<Trade> findAllByBranch_Business_IdAndLidIsTrue(UUID branch_business_id, Pageable pageable);

    List<Trade> findAllByBranch_IdOrderByCreatedAtDesc(UUID branch_id);

    @Query("SELECT t.id FROM Trade t WHERE t.branch.id = :branchId AND t.invoice = :invoice")
    UUID findTradeIdByBranchIdAndInvoice(@Param("branchId") UUID branchId, @Param("invoice") String invoice);

    List<Trade> findAllByCreatedAtBetweenAndBranchId(Timestamp start, Timestamp end, UUID branch_id);

    List<Trade> findAllByCreatedAtBetweenAndBranch_BusinessId(Timestamp start, Timestamp end, UUID businessId);

    List<Trade> findAllByBranch_Business_IdOrderByCreatedAtDesc(UUID businessId);



    double countAllByBranchIdAndCreatedAtBetween(UUID branch_id, Timestamp startDate, Timestamp endDate);

    double countAllByBranch_BusinessIdAndCreatedAtBetween(UUID branch_business_id, Timestamp startDate, Timestamp endDate);

    // trade total sum by branch id
    @Query(value = "select sum (t.totalSum) from Trade t where t.branch.id = :branchId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalSum(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // trade total sum by business id
    @Query(value = "select sum (t.totalSum) from Trade t where t.branch.business.id = :businessId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalSumByBusiness(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // trade total profit sum by branch id
    @Query(value = "select sum (t.totalProfit) from Trade t where t.branch.id = :branchId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalProfit(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // trade total profit sum by business id
    @Query(value = "select sum (t.totalProfit) from Trade t where t.branch.business.id = :businessId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalProfitByBusinessId(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select count(t) from Trade t where t.branch.id = :branchId and t.createdAt between :startDate and :endDate")
    Long countAllByBranchId(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select count(t) from Trade t where t.branch.business.id = :businessId and t.createdAt between :startDate and :endDate")
    Long countAllByBusinessId(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select count(distinct t.customer.id) from Trade t where t.branch.business.id = :businessId and t.createdAt between :startDate and :endDate")
    Long countDistinctCustomersByBusinessId(@Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("select count(distinct t.customer.id) from Trade t where t.branch.id = :branchId and t.createdAt between :startDate and :endDate")
    Long countDistinctCustomersByBranchId(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);


    @Query(value = "select sum (t.totalSum) from Trade t where t.payMethod.id = :payMethodId and t.branch.id = :branchId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalPayment(@Param("payMethodId") UUID payMethodId, @Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "select sum (t.totalSum) from Trade t where t.payMethod.id = :payMethodId and t.branch.business.id = :businessId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalPaymentByBusiness(@Param("payMethodId") UUID payMethodId, @Param("businessId") UUID businessId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);


    @Query(value = "SELECT SUM(t.debtSum) FROM Trade t WHERE t.branch.id = :branchId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Double totalDebtSum(@Param("branchId") UUID branchId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT SUM(t.totalSum) FROM Trade t WHERE t.customer.id = :customerId")
    Double totalSumByCustomer(@Param("customerId") UUID customerId);

    @Query(value = "SELECT SUM(t.totalProfit) FROM Trade t WHERE t.customer.id = :customerId")
    Double totalProfitByCustomer(@Param("customerId") UUID customerId);

    @Query(value = "SELECT SUM(t.paidSum) FROM Trade t WHERE t.id = :tradeId")
    Double totalPaidSum(@Param("tradeId") UUID tradeId);


    List<Trade> findAllByCustomer_Id(UUID customer_id);

    List<Trade> findAllByCustomerIdAndDebtSumIsNotOrderByCreatedAtAsc(UUID customerId, Double amount);


    List<Trade> findAllByCreatedAtBetweenAndCustomer_Id(Timestamp startTimestamp, Timestamp endTimestamp, UUID customer_id);

    String getTraderNameById(UUID traderId);

    @Query(value = "SELECT SUM(total_sum) FROM trade WHERE created_at BETWEEN ?1 AND ?2 AND branch_id = ?3", nativeQuery = true)
    Double totalSumByCreatedAtBetweenAndBranchId(Timestamp from, Timestamp to, UUID branch_id);

    Optional<Trade> findFirstByBranchIdOrderByCreatedAtDesc(UUID branchId);

    Page<Trade> findAllByBranchId(UUID branchId, Pageable pageable);

    Page<Trade> findAllByBranchIdAndCreatedAtBetween(UUID branchId, Date start, Date end, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessId(UUID businessId, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessIdAndCreatedAtBetween(UUID businessId, Date startDate, Date endDate, Pageable pageable);

    Page<Trade> findAllByBranchIdAndBacking(UUID branchId, boolean backing, Pageable pageable);

    Page<Trade> findAllByBranchIdAndBackingAndCreatedAtBetween(UUID branchId, boolean backing, Date start, Date end, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessIdAndBacking(UUID businessId, boolean backing, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessIdAndBackingAndCreatedAtBetween(
            UUID businessId,
            boolean backing,
            Date startDate,
            Date endDate,
            Pageable pageable
    );


    Page<Trade> findAllByBranch_BusinessIdAndInvoiceContainingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCase(UUID businessId, String invoice, UUID businessId2, String name, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessIdAndInvoiceContainingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndCreatedAtBetween(
            UUID businessId,
            String invoice,
            UUID businessId2,
            String name,
            Date startDate,
            Date endDate,
            Pageable pageable
    );


    Page<Trade> findAllByBranchIdAndInvoiceContainingOrBranchIdAndCustomer_NameContainingIgnoreCase(UUID branchId, String invoice, UUID branchId2, String name, Pageable pageable);

    Page<Trade> findAllByBranchIdAndInvoiceContainingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndCreatedAtBetween(
            UUID branchId,
            String invoice,
            UUID branchId2,
            String name,
            Date startDate,
            Date endDate,
            Pageable pageable
    );


    Page<Trade> findAllByBranch_BusinessIdAndInvoiceContainingAndBackingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndBacking(UUID businessId, String invoice, boolean backing, UUID businessId2, String name, boolean backing2, Pageable pageable);

    Page<Trade> findAllByBranch_BusinessIdAndInvoiceContainingAndBackingOrBranch_BusinessIdAndCustomer_NameContainingIgnoreCaseAndBackingAndCreatedAtBetween(
            UUID businessId,
            String invoice,
            boolean backing,
            UUID businessId2,
            String name,
            boolean backing2,
            Date startDate,
            Date endDate,
            Pageable pageable
    );


    Page<Trade> findAllByBranchIdAndInvoiceContainingAndBackingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndBacking(UUID branchId, String invoice, boolean backing, UUID branchId2, String name, boolean backing2, Pageable pageable);

    Page<Trade> findAllByBranchIdAndInvoiceContainingAndBackingOrBranchIdAndCustomer_NameContainingIgnoreCaseAndBackingAndCreatedAtBetween(
            UUID branchId,
            String invoice,
            boolean backing,
            UUID branchId2,
            String name,
            boolean backing2,
            Date startDate,
            Date endDate,
            Pageable pageable
    );


    List<Trade> findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalse(UUID branchId, UUID id);

    List<Trade> findAllByBranch_IdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID branch_id, Date startDate, Date endDate);

    List<Trade> findAllByBranch_IdAndPayMethodIdAndDollarTradeIsFalseAndDifferentPaymentIsFalseAndCreatedAtBetween(UUID branchId, UUID id, Date startDate, Date endDate);

    @Query(nativeQuery = true, value = "SELECT sum(t.total_sum - t.paid_sum) as totalAmount \n" +
                                       "FROM trade t \n" +
                                       "WHERE (t.branch_id = :branchId) AND \n" +
                                       "    ((t.payment_status_id = :payStatusId) OR (t.payment_status_id = :payStatusId2))")
    Double findAllNationTradeSum(UUID payStatusId, UUID payStatusId2, UUID branchId);

    @Query(nativeQuery = true, value = "SELECT sum(p.paid_sum) as allSumPriceDiferentPay\n" +
                                       "FROM payment p\n" +
                                       "         JOIN trade t on t.id = p.trade_id \n" +
                                       "where t.different_payment = true \n" +
                                       "  AND p.is_pay_dollar = false \n" +
                                       "  and p.pay_method_id = ?1 and t.branch_id = ?2")
    Double findAllAmountSumDifferentPayment(UUID payMethodId, UUID branchId);

    @Query(nativeQuery = true, value = "SELECT sum(p.paid_sum) as allSumPriceDiferentPay \n" +
                                       "FROM payment p\n" +
                                       "         JOIN trade t on t.id = p.trade_id \n" +
                                       "where t.different_payment = true \n" +
                                       "  AND p.is_pay_dollar = false \n" +
                                       "  and p.pay_method_id = ?1 and t.branch_id = ?2 \n" +
                                       "  and t.created_at between ?3 and ?4")
    Double findAllAmountSumDifferentPaymentAndSearchDate(UUID payMethodId, UUID branchId, Date startDate, Date endDate);

    @Query(nativeQuery = true, value = "SELECT sum(p.paid_sum_dollar) as allDollarPriceDiferentPay \n" +
                                       "FROM payment p \n" +
                                       "            JOIN trade t on t.id = p.trade_id \n" +
                                       "            where t.different_payment = true \n" +
                                       "            AND p.is_pay_dollar = true \n" +
                                       "            and p.pay_method_id = ?1  and t.branch_id = ?2")
    Double findAllAmountDollarDifferentPayment(UUID payMethodId, UUID branchId);

    @Query(nativeQuery = true, value = "SELECT sum(p.paid_sum_dollar) as allDollarPriceDiferentPay \n" +
                                       "FROM payment p \n" +
                                       "         JOIN trade t on t.id = p.trade_id \n" +
                                       "where t.different_payment = true \n" +
                                       "  AND p.is_pay_dollar = true \n" +
                                       "  and p.pay_method_id = ?1 and t.branch_id = ?2 \n" +
                                       "  and t.created_at between ?3 and ?4")
    Double findAllAmountDollarDifferentPaymentAndSearchDate(UUID payMethodId, UUID branchId, Date startDate, Date endDate);

    @Query(nativeQuery = true, value = "SELECT sum(t.total_sum - t.paid_sum) as totalAmount \n" +
                                       "FROM trade t \n" +
                                       "WHERE (t.branch_id = :branchId) AND \n" +
                                       "    ((t.payment_status_id = :payStatusId) OR (t.payment_status_id = :payStatusId2)) \n" +
                                       "  AND \n" +
                                       "    (t.created_at BETWEEN :startDate AND :endDate)")
    Double findAllNationTradeSumByDate(UUID payStatusId, UUID payStatusId2, UUID branchId, Date startDate, Date endDate);
    @Query(nativeQuery = true, value = "SELECT t.created_at as arrivaltime FROM trade t JOIN customer c on c.id = t.customer_id where c.branch_id = :branchId order by t.created_at")
    List<MonthProjection> getAllMonths(UUID branchId);


    @Query("select count(t) from Trade t where t.createdAt between :startDate and :endDate")
    long countTotalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);


}