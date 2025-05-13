package uz.dizgo.erp.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Cash;

public interface CashRepository extends JpaRepository<Cash, UUID> {
	Optional<Cash> findTopByBranchIdOrderByCreatedAtDesc(UUID branchId);

}