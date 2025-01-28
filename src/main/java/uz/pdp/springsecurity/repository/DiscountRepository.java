package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Discount;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    List<Discount> findAll(Specification<Discount> spec);

    @Query("SELECT d FROM Discount d WHERE d.endDate BETWEEN :startOfDay AND :endOfDay")
    List<Discount> findExpiredDiscounts(@Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT d FROM Discount d WHERE d.startDate BETWEEN :startOfDay AND :endOfDay")
    List<Discount> findScheduledDiscounts(@Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);

}
