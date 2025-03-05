package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Navigation;

import java.util.Optional;
import java.util.UUID;

public interface NavigationRepository extends JpaRepository<Navigation, UUID> {
    Optional<Navigation> findByBranchId(UUID branchId);

    void deleteByBranchId(UUID branchId);

    boolean existsByBranchId(UUID branchId);
}
