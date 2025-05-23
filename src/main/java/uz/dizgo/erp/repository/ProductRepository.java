package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.payload.ProductResponseDTO;
import uz.dizgo.erp.payload.ProductShortDto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByBarcodeAndBusinessIdAndActiveTrue(String barcode, UUID businessId);

    boolean existsByBarcodeAndBusinessId(String barcode, UUID businessId);

    boolean existsByBarcodeAndBusinessIdAndIdIsNotAndActiveTrue(String barcode, UUID businessId, UUID productId);

    Optional<Product> findByBarcodeAndBusinessId(String barcode, UUID business_id);

    @Query(value = "SELECT * FROM product WHERE barcode = :barcode AND business_id = :businessId", nativeQuery = true)
    Optional<Product> findByBarcodeAndBusinessIdAndActiveFalseAndDeletedTrue(
            @Param("barcode") String barcode,
            @Param("businessId") UUID businessId
    );

    @Query("""
                select sum(p.buyPrice * w.amount)
                from Product p
                join Warehouse w on w.product.id = p.id
                join branches b on w.branch.id = b.id
                where b.business.id = :businessId
                and p.createdAt between :startDate and :endDate
                 and w.amount > 0 and p.salePrice > 0
            """)
    Double totalProductBuyPrice(@Param("businessId") UUID businessId,
                                 @Param("startDate") Timestamp startDate,
                                 @Param("endDate") Timestamp endDate);


    @Query("""
                select sum(p.buyPrice * w.amount)
                from Product p
                join p.branch b
                join Warehouse w on w.product = p and w.branch = b
                where b.id = :branchId
                and p.createdAt between :startDate and :endDate
                and w.amount > 0 and p.salePrice > 0
            """) Double totalProductBuyPriceByBranch(@Param("branchId") UUID branchId,
                                         @Param("startDate") Timestamp startDate,
                                         @Param("endDate") Timestamp endDate);


    @Query("""
                select sum(p.salePrice * w.amount)
                from Product p
                join Warehouse w on w.product.id = p.id
                join branches b on w.branch.id = b.id
                where b.business.id = :businessId
                and p.createdAt between :startDate and :endDate
                and w.amount > 0 and p.salePrice > 0
            """)
    Double totalProductSalePrice(@Param("businessId") UUID businessId,
                                 @Param("startDate") Timestamp startDate,
                                 @Param("endDate") Timestamp endDate);


    @Query("""
                select sum(p.salePrice * w.amount)
                from Product p
                join p.branch b
                join Warehouse w on w.product = p and w.branch = b
                where b.id = :branchId
                and p.createdAt between :startDate and :endDate
                 and w.amount > 0 and p.salePrice > 0
            """)
    Double totalProductSalePriceByBranch(@Param("branchId") UUID branchId,
                                         @Param("startDate") Timestamp startDate,
                                         @Param("endDate") Timestamp endDate);


    List<Product> findAllByBrandIdAndCategoryIdAndBranchIdAndActiveTrue(UUID brand_id, UUID category_id, UUID branchId);

    boolean existsByPluCodeAndBusiness_Id(String pluCode, UUID business_id);

    List<Product> findAllByCategoryIdAndBranchIdAndActiveTrue(UUID category_id, UUID branch_id);

    @Query("SELECT new uz.dizgo.erp.payload.ProductShortDto(p.id, " +
            "COALESCE(pt.name, p.name), p.brand.id, p.category.id, p.salePrice) " +
            "FROM Product p " +
            "LEFT JOIN ProductTranslate pt ON pt.product = p " +
            "LEFT JOIN Language l ON pt.language = l " +
            "WHERE p.category.id = :categoryId AND p.active = true " +
            "AND (l.code = :languageCode OR l.code IS NULL) " +
            "ORDER BY COALESCE(pt.name, p.name) ASC")
    List<ProductShortDto> findAllByCategoryIdAndActiveOrderByNameAsc(
            @Param("categoryId") UUID categoryId,
            @Param("languageCode") String languageCode);

    @Query("SELECT new uz.dizgo.erp.payload.ProductShortDto(p.id, " +
            "COALESCE(pt.name, p.name), p.brand.id, p.category.id, p.salePrice) " +
            "FROM Product p " +
            "LEFT JOIN ProductTranslate pt ON pt.product = p " +
            "LEFT JOIN Language l ON pt.language = l " +
            "WHERE p.brand.id = :brandId AND p.active = true " +
            "AND (l.code = :languageCode OR l.code IS NULL) " +
            "ORDER BY COALESCE(pt.name, p.name) ASC")
    List<ProductShortDto> findAllByBrandIdAndActiveOrderByNameAsc(
            @Param("brandId") UUID brandId,
            @Param("languageCode") String languageCode);

    Page<Product> findAllByCategory_IdAndBranch_IdAndActiveTrue(UUID category_id, UUID branch_id, Pageable pageable);

    List<Product> findAllByBrandIdAndBusinessIdAndActiveTrue(UUID brand_id, UUID businessId);

    List<Product> findAllByBranchIdAndActiveIsTrue(UUID branch_id);

    Page<Product> findAllByBranch_IdAndActiveIsTrue(UUID branch_id, Pageable pageable);

    List<Product> findAllByBranchIdAndActiveIsTrueAndNameContainingIgnoreCaseOrBarcodeContainingIgnoreCase(UUID branch_id, String name, String barcode);

    List<Product> findAllByBranchIdAndBarcodeOrNameAndActiveTrue(UUID branch_id, String barcode, String name);

    Optional<Product> findByBarcodeAndBranch_IdAndActiveTrue(String barcode, UUID receivedBranch);

    List<Product> findAllByBusiness_IdAndActiveTrue(UUID businessId);

    List<Product> findAllByBusiness_IdAndActiveTrueAndIsGlobalTrue(UUID businessId);

    List<Product> findAllByBranchIdAndActiveTrue(UUID branch_id);

    List<Product> findAllByBusinessIdAndActiveFalse(UUID businessId);

    List<Product> findAllByCategoryIdAndBusinessIdAndActiveTrue(UUID categoryId, UUID businessId);

    List<Product> findAllByBrandIdAndBranchIdAndActiveTrue(UUID brandId, UUID branchId);

    List<Product> findAllByBrandIdAndCategoryIdAndBusinessIdAndActiveTrue(UUID brandId, UUID categoryId, UUID businessId);

    List<Product> findAllByBusinessIdAndActiveTrueAndBuyDollarTrueOrSaleDollarTrue(UUID businessId);

    int countAllByBranchId(UUID branchId);

    Page<Product> findAllByBranch_IdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndBarcodeContainingIgnoreCaseAndActiveTrue(UUID branchId1, String name, UUID branchId2, String barcode, Pageable pageable);

    Page<Product> findAllByBusinessIdAndActiveTrue(UUID businessId, Pageable pageable);

    Page<Product> findAllByBranch_IdAndActiveTrue(UUID branchId, Pageable pageable);

    Page<Product> findAllByRastaList_IdAndActiveTrue(UUID rastaListId, Pageable pageable);

    List<Product> findAllByBarcode(String barcode);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    long countTotalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.clone = true AND p.createdAt BETWEEN :startDate AND :endDate")
    long countCloneBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.isGlobal = false AND p.createdAt BETWEEN :startDate AND :endDate")
    long countGlobalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @EntityGraph(attributePaths = {"translations"})
    @Query("SELECT p FROM Product p " +
            "WHERE p.active = true " +
            "AND (p.business.id = :businessId) " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR  (p.business.id = :businessId) and LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> findAllProductsWithTranslates(
            @Param("businessId") UUID businessId,
            @Param("keyword") String keyword);

    @Query("SELECT new uz.dizgo.erp.payload.ProductResponseDTO( " +
            "p.id, " +
            "COALESCE(pt.name, p.name), " +
            "p.salePrice, p.barcode, p.MXIKCode, " +
            "p.discount) " +
            "FROM Product p " +
            "LEFT JOIN p.translations pt ON pt.language.code = :languageCode " +
            "WHERE p.business.id IN (SELECT b.business.id FROM branches b WHERE b.id = :branchId) " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR p.business.id IN (SELECT b.business.id FROM branches b WHERE b.id = :branchId) AND p.barcode = :keyword)" +
            "OR p.business.id IN (SELECT b.business.id FROM branches b WHERE b.id = :branchId) " +
            "AND (LOWER(pt.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "GROUP BY p.id, pt.name, p.salePrice, p.barcode, p.MXIKCode, p.discount")
    List<ProductResponseDTO> findProductsByBranchIdAndKeyword(
            @Param("branchId") UUID branchId,
            @Param("keyword") String keyword,
            @Param("languageCode") String languageCode);

    @Query("SELECT new uz.dizgo.erp.payload.ProductResponseDTO( " +
            "p.id, " +
            "COALESCE(pt.name, p.name), " +
            "p.salePrice, p.barcode, p.MXIKCode, " +
            "p.discount) " +
            "FROM Product p " +
            "LEFT JOIN p.translations pt ON pt.language.code = :languageCode " +
            "JOIN p.branch b " +
            "WHERE b.id = :branchId AND p.barcode = :barcode " +
            "GROUP BY p.id, pt.name, p.salePrice, p.barcode, p.MXIKCode, p.discount")
    Optional<ProductResponseDTO> findByBranchIdAndBarcodeDto(
            @Param("branchId") UUID branchId,
            @Param("barcode") String barcode,
            @Param("languageCode") String languageCode);


    // Business ID bo‘yicha mahsulot qidirish
    @Query("""
            SELECT p FROM Product p LEFT JOIN ProductTranslate pt ON pt.product.id = p.id WHERE p.business.id = :businessId AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR  p.business.id = :businessId  and LOWER(pt.name) LIKE LOWER(CONCAT('%', :search, '%')) OR p.business.id = :businessId  and p.barcode = :search) group by p.id""")
    Page<Product> findByBusinessIdAndNameContainingIgnoreCase(@Param("businessId") UUID businessId,
                                                              @Param("search") String search,
                                                              Pageable pageable);

    // Branch ID bo‘yicha mahsulot qidirish (ManyToMany bo‘lgani uchun JOIN ishlatamiz)
    @Query("""
            SELECT p FROM Product p JOIN p.branch b LEFT JOIN ProductTranslate pt ON pt.product.id = p.id WHERE b.id = :branchId AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR b.id = :branchId AND LOWER(pt.name) LIKE LOWER(CONCAT('%', :search, '%')) OR b.id = :branchId AND p.barcode = :search) group by p.id""")
    Page<Product> findByBranchIdAndNameContainingIgnoreCase(@Param("branchId") UUID branchId,
                                                            @Param("search") String search,
                                                            Pageable pageable);

    @Query("select count(p.id) from Product p " +
            "join p.branch b " +
            "where b.id = :branchId " +
            "and p.active = true " +
            "and p.deleted = false ")
    Long countProductsByBranch(
            @Param("branchId") UUID branchId);

    @Query("select count(p.id) from Product p " +
            "where p.business.id = :businessId " +
            "and p.active = true " +
            "and p.deleted = false ")
    Long countProductsByBusiness(
            @Param("businessId") UUID businessId);
}