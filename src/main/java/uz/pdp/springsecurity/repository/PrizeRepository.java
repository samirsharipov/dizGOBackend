package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Prize;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrizeRepository extends JpaRepository<Prize, UUID> {
    List<Prize> findAllByBranchId(UUID branchId);
    List<Prize> findAllByBranchIdAndUserIdAndGivenTrueOrderByDateDesc(UUID branchId, UUID userId);
    List<Prize> findAllByBranchIdAndUserIdAndActiveTrueAndGivenTrue(UUID branchId, UUID userId);
    Optional<Prize> findByUserIdAndBranchIdAndTaskTrueAndGivenFalse(UUID userId, UUID branchId);
    Optional<Prize> findByUserIdAndBranchIdAndLidTrueAndGivenFalse(UUID userId, UUID branchId);
}
