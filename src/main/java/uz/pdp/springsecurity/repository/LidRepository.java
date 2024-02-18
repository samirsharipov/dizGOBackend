package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Lid;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface LidRepository extends JpaRepository<Lid, UUID> {
    List<Lid> findAllByBusiness_Id(UUID business_id);
    Page<Lid> findAllByLidStatus_IdAndDeleteIsFalse(UUID lidStatus_id, Pageable pageable);
    List<Lid> findAllByLidStatusId(UUID lidStatus_id);
    List<Lid> findAllBySourceId(UUID source_id);
    List<Lid> findAllByLidStatus_OrginalName(String lidStatus_orginalName);
    int countByLidStatusIdAndDeleteIsFalse(UUID lidStatus_id);
    Page<Lid> findAllByLidStatusIdAndSourceIdAndDeleteIsFalse(UUID lidStatus_id, UUID source_id, Pageable pageable);

    Page<Lid> findAllByLidStatusIdAndCreatedAtBetweenAndDeleteIsFalse(UUID lidStatus_id, Timestamp startDate, Timestamp endDate, Pageable pageable);

    Page<Lid> findAllByLidStatusIdAndSourceIdAndCreatedAtBetweenAndDeleteIsFalse(UUID lidStatus_id, UUID source_id, Timestamp startDate, Timestamp endDate, Pageable pageable);

    Page<Lid> findAllByBusinessIdAndDeleteIsFalse(UUID business_id, Pageable pageable);

    Page<Lid> findAllByBusinessIdAndSourceIdAndDeleteIsFalse(UUID business_id, UUID source_id, Pageable pageable);

    Page<Lid> findAllByBusinessIdAndSourceIdAndCreatedAtBetweenAndDeleteIsFalse(UUID business_id, UUID source_id, Timestamp createdAt, Timestamp createdAt2, Pageable pageable);

    Page<Lid> findAllByBusinessIdAndCreatedAtBetweenAndDeleteIsFalse(UUID business_id, Timestamp startTime, Timestamp endTime, Pageable pageable);

    int countAllByCreatedAtBetweenAndBusinessId(Timestamp from, Timestamp to, UUID businessId);
}
