package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProjectStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, UUID> {

    List<ProjectStatus> findAllByBranchId(UUID branch_id);
    ProjectStatus findByNameAndBranchId(String name,UUID branch_id);
}
