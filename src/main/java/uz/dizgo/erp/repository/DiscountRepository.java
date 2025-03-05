package uz.dizgo.erp.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Discount;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    List<Discount> findAll(Specification<Discount> spec);

    @Query("SELECT d FROM Discount d WHERE d.endDate BETWEEN :startOfDay AND :endOfDay")
    List<Discount> findExpiredDiscounts(@Param("startOfDay") Timestamp startOfDay,
                                        @Param("endOfDay") Timestamp endOfDay);

    @Query("SELECT d FROM Discount d WHERE d.startDate BETWEEN :startOfDay AND :endOfDay")
    List<Discount> findScheduledDiscounts(@Param("startOfDay") Timestamp startOfDay,
                                          @Param("endOfDay") Timestamp endOfDay);


    @Query("SELECT d FROM Discount d JOIN d.products p JOIN d.branches b " +
            "WHERE p.id = :productId AND b.id = :branchId " +
            "AND d.active = true AND d.deleted = false")
    Optional<Discount> findByProductIdAndBranchId(@Param("productId") UUID productId,
                                                  @Param("branchId") UUID branchId);



    @Query("SELECT COUNT(d) > 0 FROM Discount d JOIN d.products p JOIN d.branches b " +
            "WHERE p.id = :productId AND b.id = :branchId")
    boolean existsByProductIdAndBranchId(@Param("productId") UUID productId,
                                         @Param("branchId") UUID branchId);

}
