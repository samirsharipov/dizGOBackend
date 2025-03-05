package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PenaltyTemplate;

import java.util.List;
import java.util.UUID;

public interface PenaltyTemplateRepository extends JpaRepository<PenaltyTemplate, UUID> {

    List<PenaltyTemplate> findAllByBranchId(UUID branchId);
}
