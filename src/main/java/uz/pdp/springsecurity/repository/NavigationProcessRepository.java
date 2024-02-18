package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.NavigationProcess;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NavigationProcessRepository extends JpaRepository<NavigationProcess, UUID> {
    List<NavigationProcess> findAllByBranchIdAndDateBetweenAndRealIsFalse(UUID branch_id, Date from, Date to);
    List<NavigationProcess> findAllByBranchIdAndDateBetweenAndRealIsTrue(UUID branch_id, Date from, Date to);
    Optional<NavigationProcess> findFirstByBranchIdAndRealTrueOrderByCreatedAtDesc(UUID branchId);
    void deleteByBranchId(UUID branchId);
}
