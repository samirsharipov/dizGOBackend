package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.OutlayCategory;

import java.util.List;
import java.util.UUID;

public interface OutlayCategoryRepository extends JpaRepository<OutlayCategory, UUID> {
    List<OutlayCategory> findAllByBranch_Id(UUID branch_id);

    List<OutlayCategory> findAllByBranchBusinessId(UUID branch_business_id);
}
