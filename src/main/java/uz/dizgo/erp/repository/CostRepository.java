package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Cost;

import java.util.List;
import java.util.UUID;

public interface CostRepository extends JpaRepository<Cost, UUID> {
    List<Cost> findAllByContentId(UUID contentId);

    List<Cost> findAllByProductionId(UUID productionId);

    void deleteAllByContentId(UUID contentId);
}
