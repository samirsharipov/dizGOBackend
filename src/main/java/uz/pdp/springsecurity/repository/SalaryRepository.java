package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Salary;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, UUID> {
    Optional<Salary> findByUserIdAndBranch_IdAndActiveTrue(UUID userId, UUID branchId);

    boolean existsByUserIdAndBranch_IdAndActiveTrue(UUID userId, UUID branchId);

    Optional<Salary> findByIdAndActiveTrue(UUID salaryId);

    List<Salary> findAllByBranchIdAndActiveTrue(UUID branchId);

    List<Salary> findAllByUserIdAndBranchId(UUID userId, UUID branchId);

    @Query(value = "SELECT SUM(salary) FROM salary WHERE created_at BETWEEN ?1 AND  ?2 AND branch_id = ?3", nativeQuery = true)
    Double salaryByCreatedAtBetweenAndBranchId(Timestamp from, Timestamp to, UUID branch_id);
}
