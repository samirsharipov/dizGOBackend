package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByBarcodeAndBusinessIdAndActiveTrue(String barcode, UUID businessId);

    boolean existsByBarcodeAndBusinessIdAndIdIsNotAndActiveTrue(String barcode, UUID businessId, UUID productId);

    List<Product> findAllByBrandIdAndCategoryIdAndBranchIdAndActiveTrue(UUID brand_id, UUID category_id, UUID branchId);

    List<Product> findAllByBrandIdAndActiveIsTrue(UUID brand_id);


    Optional<Product> findAllByBarcodeAndBranchIdAndActiveTrue(String barcode, UUID branch_id);

    List<Product> findAllByCategoryIdAndBranchIdAndActiveTrue(UUID category_id, UUID branch_id);

    Page<Product> findAllByCategory_IdAndBranch_IdAndActiveTrue(UUID category_id, UUID branch_id, Pageable pageable);

    List<Product> findAllByBrandIdAndBusinessIdAndActiveTrue(UUID brand_id, UUID businessId);

    List<Product> findAllByBranchIdAndActiveIsTrue(UUID branch_id);

    Page<Product> findAllByBranch_IdAndActiveIsTrue(UUID branch_id, Pageable pageable);

    List<Product> findAllByBranchIdAndActiveIsTrueAndNameContainingIgnoreCase(UUID branch_id, String name);

    List<Product> findAllByBranchIdAndActiveIsTrueAndNameContainingIgnoreCaseOrBarcodeContainingIgnoreCase(UUID branch_id, String name, String barcode);

    List<Product> findAllByBranchIdAndActiveIsTrueAndBarcodeContainingIgnoreCase(UUID branch_id, String name);

//    Page<Product> findAllByBranchIdAndNameContainingIgnoreCaseOrBranchIdAndBarcodeContainingIgnoreCase(
//            UUID branchId, String name, UUID branchId2, String barcode, Pageable pageable);

    Page<Product> findAllByBranchIdAndNameContainingIgnoreCaseOrBranchIdAndBarcodeContainingIgnoreCaseOrBranchIdAndCategoryIdAndActiveTrue
            (UUID branch_id, String name, UUID branch_id2, String barcode, UUID branch_id3, UUID category_id, Pageable pageable);

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


    Page<Product> findAllByBusinessIdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndBarcodeContainingIgnoreCaseAndActiveTrue(UUID busId1, String name, UUID busId2, String barcode, Pageable pageable);

    Page<Product> findAllByBranch_IdAndNameContainingIgnoreCaseAndActiveTrueOrBusinessIdAndBarcodeContainingIgnoreCaseAndActiveTrue(UUID branchId1, String name, UUID branchId2, String barcode, Pageable pageable);

    Page<Product> findAllByBusinessIdAndCategoryIdAndBrandIdAndActiveTrue(UUID businessId, UUID categoryId, UUID brandId, Pageable pageable);

    Page<Product> findAllByBranch_IdAndCategoryIdAndBrandIdAndActiveTrue(UUID branchId, UUID categoryId, UUID brandId, Pageable pageable);

    Page<Product> findAllByBusinessIdAndCategoryIdAndActiveTrue(UUID businessId, UUID categoryId, Pageable pageable);

    Page<Product> findAllByBranch_IdAndCategoryIdAndActiveTrue(UUID branchId, UUID categoryId, Pageable pageable);

    Page<Product> findAllByBusinessIdAndBrandIdAndActiveTrue(UUID businessId, UUID brandId, Pageable pageable);

    Page<Product> findAllByBranch_IdAndBrandIdAndActiveTrue(UUID branchId, UUID brandId, Pageable pageable);

    Page<Product> findAllByBusinessIdAndActiveTrue(UUID businessId, Pageable pageable);

    Page<Product> findAllByBranch_IdAndActiveTrue(UUID branchId, Pageable pageable);

    Page<Product> findAllByRastaList_IdAndActiveTrue(UUID rastaListId, Pageable pageable);

}
