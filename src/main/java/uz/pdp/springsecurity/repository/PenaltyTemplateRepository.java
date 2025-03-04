package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.PenaltyTemplate;

import java.util.List;
import java.util.UUID;

public interface PenaltyTemplateRepository extends JpaRepository<PenaltyTemplate, UUID> {

    List<PenaltyTemplate> findAllByBranchId(UUID branchId);
}
