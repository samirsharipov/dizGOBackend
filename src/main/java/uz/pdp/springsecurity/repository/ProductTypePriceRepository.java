package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductTypePrice;

import java.util.List;
import java.util.UUID;

public interface ProductTypePriceRepository extends JpaRepository<ProductTypePrice, UUID> {
    List<ProductTypePrice> findAllByProductIdAndActiveTrue(UUID product_id);
    List<ProductTypePrice> findAllByProduct_BranchIdAndActiveTrue(UUID product_branch_id);
    boolean existsByProduct_ActiveAndBarcodeAndProduct_BusinessIdAndActiveTrue(boolean product_active, String barcode, UUID product_business_id);
    List<ProductTypePrice> findAllByProduct_BusinessIdAndActiveTrue(UUID businessId);
    List<ProductTypePrice> findAllByProduct_CategoryIdAndProduct_BranchIdAndProduct_ActiveTrue(UUID product_category_id, UUID product_branch_id);
    List<ProductTypePrice> findAllByProduct_BranchIdAndProduct_ActiveIsTrue(UUID product_branch_id);

    List<ProductTypePrice> findAllByProduct_Category_IdAndProduct_Branch_IdAndProduct_ActiveIsTrue(UUID product_category_id, UUID product_branch_id);
    List<ProductTypePrice> findAllByProduct_Brand_IdAndProduct_Branch_IdAndProduct_ActiveIsTrue(UUID product_brand_id, UUID product_branch_id);
    List<ProductTypePrice> findAllByProduct_BrandIdAndProduct_CategoryIdAndProduct_Branch_IdAndProduct_ActiveIsTrue(UUID product_brandId, UUID product_category_id, UUID product_branch_id);
    boolean existsByBarcodeAndProduct_BusinessIdAndActiveTrue(String barcode, UUID businessId);
    boolean existsByBarcodeAndProduct_BusinessIdAndIdIsNotAndActiveTrue(String barcode, UUID businessId, UUID productTypePriceId);
    List<ProductTypePrice> findAllByProduct_BranchIdAndProduct_BrandIdAndActiveTrue(UUID product_branch_id, UUID product_brand_id);
}