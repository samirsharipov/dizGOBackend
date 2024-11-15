package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.BranchCategory;

import java.util.UUID;

public interface BranchCategoryRepository extends JpaRepository<BranchCategory, UUID> {
}
