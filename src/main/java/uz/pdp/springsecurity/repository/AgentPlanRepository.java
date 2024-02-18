package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.AgentPlan;

import java.util.UUID;

public interface AgentPlanRepository extends JpaRepository<AgentPlan, UUID> {
    Page<AgentPlan> findAllByBranch_Business_IdAndActiveTrue(UUID businessId, Pageable pageable);

    Page<AgentPlan> findAllByBranch_IdAndActiveTrue(UUID branch_id, Pageable pageable);
}
