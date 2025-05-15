package uz.dizgo.erp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Cash;

public interface CashRepository extends JpaRepository<Cash, UUID> {
	List<Cash> findAllByBranchId(UUID branchId);

	Optional<Cash> findByBranchId(UUID branchId);
}