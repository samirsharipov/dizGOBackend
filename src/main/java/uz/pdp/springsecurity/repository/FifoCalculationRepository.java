package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.FifoCalculation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FifoCalculationRepository extends JpaRepository<FifoCalculation, UUID> {
    List<FifoCalculation> findAllByBranchIdAndProductIdAndActiveTrueOrderByCreatedAt(UUID branchId, UUID productId);


    List<FifoCalculation> findFirst20ByBranchIdAndProductIdOrderByCreatedAtDesc(UUID branchId, UUID productId);


    Optional<FifoCalculation> findByPurchaseProductId(UUID purchaseProductId);

    List<FifoCalculation> findAllByBranchIdAndActiveTrue(UUID branchId);

    List<FifoCalculation> findAllByBranch_BusinessIdAndActiveTrue(UUID businessId);

    Page<FifoCalculation> findAllByBranchIdAndProductIdAndProductionIsNotNullOrderByCreatedAtDesc(UUID branchId, UUID productId, Pageable pageable);

    @Query(value = "SELECT remain_amount FROM fifo_calculation WHERE purchase_product_id = ?1", nativeQuery = true)
    Double remainQuantityByPurchaseProductId(UUID purchaseProductId);

    void deleteAllByProductId(UUID productId);

    @Query(value = "SELECT SUM(remain_amount * buy_price) FROM fifo_calculation WHERE product_id = ?1", nativeQuery = true)
    Double buyPriceByProductSingle(UUID productId);

    @Query(value = "SELECT SUM(remain_amount * buy_price) FROM fifo_calculation WHERE product_id = ?1 AND branch_id = ?2", nativeQuery = true)
    Double buyPriceByProductSingleAndBranchId(UUID productId, UUID branchId);

}
