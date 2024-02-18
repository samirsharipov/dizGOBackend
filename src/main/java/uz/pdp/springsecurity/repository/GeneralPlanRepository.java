package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.GeneralPlan;

import java.util.UUID;

public interface GeneralPlanRepository extends JpaRepository<GeneralPlan, UUID> {
    Page<GeneralPlan> findAllByBranch_Business_IdAndActiveTrue(UUID businessId, Pageable pageable);

    Page<GeneralPlan> findAllByBranch_IdAndActiveTrue(UUID branch_id, Pageable pageable);
}
