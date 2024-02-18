package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductHistory;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, UUID> {
    Page<ProductHistory> findAllByBranchIdAndCreatedAtBetween(UUID branchId, Date from, Date to, Pageable pageable);
    Optional<ProductHistory> findByBranchIdAndProductIdAndCreatedAtBetween(UUID branchId, UUID productId,  Date from, Date to);
    Optional<ProductHistory> findByBranchIdAndProductTypePriceIdAndCreatedAtBetween(UUID branchId, UUID productTypePriceId, Date from, Date to);

    boolean existsAllByBranchIdAndCreatedAtBetween(UUID branchId, Date from, Date to);
}
