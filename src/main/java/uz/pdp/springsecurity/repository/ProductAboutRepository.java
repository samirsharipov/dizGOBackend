package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductAbout;

import java.util.List;
import java.util.UUID;

public interface ProductAboutRepository extends JpaRepository<ProductAbout, UUID> {
    Page<ProductAbout> findAllByProductIdOrderByCreatedAtDesc(UUID productId, Pageable pageable);
    Page<ProductAbout> findAllByProductIdAndBranchIdOrderByCreatedAtDesc(UUID productId, UUID branchId, Pageable pageable);
   List<ProductAbout> findAllByTradeId(UUID tradeId);




}
    