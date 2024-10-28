package uz.pdp.springsecurity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.TradeProduct;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TradeProductRepository extends JpaRepository<TradeProduct, UUID> {
    List<TradeProduct> findAllByProduct_Id(UUID product_id);

    List<TradeProduct> findAllByTradeIdAndCreatedAtBetweenAndProduct_MeasurementIdOrderByCreatedAtDesc(UUID trade_id, Date startDate, Date endDate, UUID measurementId);

    List<TradeProduct> findAllByTrade_BranchIdAndProduct_Id(UUID trade_branch_id, UUID product_id);

    List<TradeProduct> findAllByProduct_IdAndTrade_CustomerId(UUID product_id, UUID trade_customer_id);

    List<TradeProduct> findAllByCreatedAtBetweenAndProductId(Timestamp startDate, Timestamp endDate, UUID product_id);

    List<TradeProduct> findAllByCreatedAtBetweenAndProduct_CategoryId(Timestamp startDate, Timestamp endDate, UUID product_category_id);

    List<TradeProduct> findAllByCreatedAtBetweenAndProduct_BrandId(Timestamp startDate, Timestamp endDate, UUID product_brand_id);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_CustomerId(Timestamp startDate, Timestamp endDate, UUID trade_customer_id);

    List<TradeProduct> findAllByProduct_BrandId(UUID brandId);

    List<TradeProduct> findAllByTradeId(UUID tradeId);

    List<TradeProduct> findAllByTrade_BranchId(UUID product_branch_id);

    @Query("SELECT tp FROM TradeProduct tp WHERE tp.trade.branch.id = :branchId ORDER BY tp.createdAt")
    Page<TradeProduct> findAllByTrade_BranchIdOrderByCreatedAt(@Param("branchId") UUID branchId, Pageable pageable);

    List<TradeProduct> findAllByProduct_CategoryIdAndTrade_BranchId(UUID categoryId, UUID branchId);

    List<TradeProduct> findAllByProduct_BrandIdAndTrade_BranchId(UUID brandId, UUID branchId);

    List<TradeProduct> findAllByTrade_PayMethodIdAndTrade_BranchId(UUID payMethodId, UUID branchId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_PayMethodIdAndTrade_BranchId(Timestamp from, Timestamp to, UUID payMethodId, UUID branchId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_CustomerIdAndTrade_BranchId(Timestamp from, Timestamp to, UUID customerId, UUID branchId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_BranchId(Timestamp from, Timestamp to, UUID branchId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_CategoryId(Timestamp from, Timestamp to, UUID branchId, UUID categoryId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_BrandId(Timestamp from, Timestamp to, UUID branchId, UUID brandId);

    List<TradeProduct> findAllByProduct_CategoryIdAndProduct_BrandIdAndTrade_BranchId(UUID categoryId, UUID brandId, UUID branchId);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_BranchIdAndProduct_CategoryIdAndProduct_BrandId(Timestamp from, Timestamp to, UUID branchId, UUID categoryId, UUID brandId);

    List<TradeProduct> findAllByTrade_CustomerIdAndTrade_BranchId(UUID customerId, UUID branchId);

    List<TradeProduct> findAllByTrade_CustomerIdAndTrade_BranchIdAndTrade_PayMethodId(UUID customerId, UUID branchId, UUID payMethodId);

    List<TradeProduct> findAllByTrade_CustomerId(UUID customerId);

    List<TradeProduct> findAllByProduct_Business_IdOrderByTradedQuantity(UUID product_business_id);

    List<TradeProduct> findAllByCreatedAtBetweenAndTrade_Customer_Id(Timestamp createdAt, Timestamp createdAt2, UUID trade_customer_id);


    List<TradeProduct> findAllByProduct_CategoryId(UUID id);

    Page<TradeProduct> findAllByTrade_BranchIdAndProductIdOrderByCreatedAtDesc(UUID branchId, UUID productId, Pageable pageable);

    @Query(value = "SELECT SUM(traded_quantity) FROM trade_product WHERE created_at BETWEEN ?1 AND ?2 AND product_id = ?3 AND trade_id IN (SELECT id FROM trade WHERE branch_id = ?4)", nativeQuery = true)
    Double quantityByBranchIdAndProductIdAndCreatedAtBetween(Timestamp from, Timestamp to, UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(traded_quantity) FROM trade_product WHERE product_id = ?1", nativeQuery = true)
    Double soldQuantityByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(traded_quantity) FROM trade_product WHERE product_type_price_id IN (SELECT id FROM product_type_price WHERE product_id = ?1)", nativeQuery = true)
    Double soldQuantityByProductMany(UUID productId);

    @Query(value = "SELECT SUM(traded_quantity) FROM trade_product WHERE product_id = ?1 AND trade_id IN (SELECT id FROM trade WHERE branch_id = ?2)", nativeQuery = true)
    Double soldQuantityByProductSingleAndBranchId(UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(backing) FROM trade_product WHERE product_id = ?1", nativeQuery = true)
    Double backingByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(backing) FROM trade_product WHERE product_id = ?1 AND trade_id IN (SELECT id FROM trade WHERE branch_id = ?2)", nativeQuery = true)
    Double backingByProductSingleAndBranchId(UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(total_sale_price) FROM trade_product WHERE product_id = ?1", nativeQuery = true)
    Double soldPriceByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(total_sale_price) FROM trade_product WHERE product_id = ?1 AND trade_id IN (SELECT id FROM trade WHERE branch_id = ?2)", nativeQuery = true)
    Double soldPriceByProductSingleAndBranchId(UUID productId, UUID branchId);

    @Query(value = "SELECT SUM(profit) FROM trade_product WHERE product_id = ?1", nativeQuery = true)
    Double profitByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(profit) FROM trade_product WHERE product_id = ?1 AND trade_id IN (SELECT id FROM trade WHERE branch_id = ?2)", nativeQuery = true)
    Double profitByProductSingleAndBranchId(UUID productId, UUID branchId);

    List<TradeProduct> findAllByTrade_BranchIdAndBackingIsNotNull(UUID branchId);

    Page<TradeProduct> findAllByTrade_BranchIdAndProductIdAndBackingIsNotNullOrderByCreatedAtDesc(UUID branchId, UUID productId, Pageable pageable);

    List<TradeProduct> findAllByTrade_Customer_IdAndTrade_Branch_IdAndCreatedAtBetween(UUID customerId, UUID branchId, Timestamp startDate, Timestamp endDate);

    List<TradeProduct> findAllByTrade_Customer_IdAndTrade_Branch_IdAndProduct_IdAndCreatedAtBetween(UUID customerId, UUID branchId, UUID productId, Timestamp startDate, Timestamp endDate);
}

