package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.WorkTime;
import uz.pdp.springsecurity.payload.projections.MonthProjection;

import java.sql.Timestamp;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkTimeRepository extends JpaRepository<WorkTime, UUID> {
    boolean existsByUserIdAndBranchIdAndActiveTrue(UUID userId, UUID branchId);

    Optional<WorkTime> findByUserIdAndBranchIdAndActiveTrue(UUID userID, UUID branchId);

    List<WorkTime> findAllByUserIdAndBranchId(UUID userId, UUID branchId);

    List<WorkTime> findAllByUserIdAndBranchIdAndArrivalTimeIsBetweenOrderByCreatedAt(UUID user_id, UUID branch_id, Timestamp from, Timestamp to);

    int countAllByUserIdAndBranchIdAndArrivalTimeIsBetween(UUID user_id, UUID branch_id, Timestamp from, Timestamp to);

    void deleteAllByUserIdAndBranchIdAndActiveFalse(UUID userId, UUID branchId);
    @Query(nativeQuery = true, value = "select arrival_time as arrivaltime from work_time where branch_id = :branchId order by arrival_time")
    List<MonthProjection> getAllMonths(UUID branchId);
}
