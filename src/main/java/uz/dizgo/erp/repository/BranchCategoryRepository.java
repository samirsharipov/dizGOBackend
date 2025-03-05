package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.BranchCategory;

import java.util.UUID;

public interface BranchCategoryRepository extends JpaRepository<BranchCategory, UUID> {
}
