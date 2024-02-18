package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Navigation;

import java.util.Optional;
import java.util.UUID;

public interface NavigationRepository extends JpaRepository<Navigation, UUID> {
    Optional<Navigation> findByBranchId(UUID branchId);

    void deleteByBranchId(UUID branchId);

    boolean existsByBranchId(UUID branchId);
}
