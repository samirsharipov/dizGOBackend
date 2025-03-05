package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.OutlayCategory;

import java.util.List;
import java.util.UUID;

public interface OutlayCategoryRepository extends JpaRepository<OutlayCategory, UUID> {
    List<OutlayCategory> findAllByBranch_Id(UUID branch_id);

    List<OutlayCategory> findAllByBusinessId(UUID branch_business_id);
}
