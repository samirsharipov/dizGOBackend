package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    boolean existsByProjectTypeId(UUID projectType_id);

    List<Project> findAllByBranchId(UUID branch_id);

    Page<Project> findAllByBranchIdAndUsers_Id(UUID branch_id, UUID users_id, Pageable pageable);

    List<Project> findAllByBranch_BusinessId(UUID branch_id);

    int countAllByUsersId(UUID users_id);

    int countAllByBranchId(UUID branch_id);

    int countAllByExpiredTrue();

    int countAllByProjectStatus_NameAndUsersId(String projectStatus_name, UUID users_id);

    Page<Project> findAllByCustomerIdAndExpiredTrue(UUID customerId, Pageable pageable);

    Page<Project> findAllByCustomerIdAndProjectStatusId(UUID customerId, UUID projectStatusId, Pageable pageable);
    Page<Project> findAllByUsersIdAndCustomerIdAndProjectStatusId(UUID userId, UUID customerId, UUID projectStatusId, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(UUID typeId, UUID customerId, UUID projectStatusId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(UUID userId, UUID typeId, UUID customerId, UUID projectStatusId, Pageable pageable);

    Page<Project> findAllByProjectTypeId(UUID typeId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeId(UUID userId, UUID typeId, Pageable pageable);

    Page<Project> findAllByCustomerId(UUID customerId, Pageable pageable);
    Page<Project> findAllByUsersIdAndCustomerId(UUID userId, UUID customerId, Pageable pageable);

    Page<Project> findAllByProjectStatusId(UUID projectStatusId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectStatusId(UUID userId, UUID projectStatusId, Pageable pageable);

    Page<Project> findAllByBranch_IdAndExpiredTrue(UUID branchId, Pageable pageable);
    Page<Project> findAllByUsersIdAndBranch_IdAndExpiredTrue(UUID userId, UUID branchId, Pageable pageable);

    Page<Project> findAllByBranch_Id(UUID branchId, Pageable pageable);

    Page<Project> findAllByUsers_id(UUID users_id, Pageable pageable);

    Page<Project> findAllByProjectStatusIdAndExpiredTrue(UUID projectStatusId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectStatusIdAndExpiredTrue(UUID userId, UUID projectStatusId, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndExpiredTrue(UUID typeId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndExpiredTrue(UUID userId, UUID typeId, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndCustomerId(UUID typeId, UUID customerId, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndCustomerId(UUID userId, UUID typeId, UUID customerId, Pageable pageable);

    Page<Project> findAllByCustomerIdAndProjectStatusIdAndExpiredTrue(UUID customerId, UUID projectStatusId, Pageable pageable);
    Page<Project> findAllByUsersIdAndCustomerIdAndProjectStatusIdAndExpiredTrue(UUID userId, UUID customerId, UUID projectStatusId, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndCustomerIdAndProjectStatusId(UUID projectType_id, UUID customer_id, UUID projectStatus_id, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndCustomerIdAndProjectStatusId(UUID userId, UUID projectType_id, UUID customer_id, UUID projectStatus_id, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndCustomerIdAndExpiredTrue(UUID projectType_id, UUID customer_id, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndCustomerIdAndExpiredTrue(UUID userId, UUID projectType_id, UUID customer_id, Pageable pageable);

    Page<Project> findAllByProjectTypeIdAndProjectStatusIdAndExpiredTrue(UUID projectType_id, UUID projectStatus_id, Pageable pageable);
    Page<Project> findAllByUsersIdAndProjectTypeIdAndProjectStatusIdAndExpiredTrue(UUID userId, UUID projectType_id, UUID projectStatus_id, Pageable pageable);

    Page<Project> findByNameContainingIgnoreCaseAndBranchId(String name, UUID branch_id, Pageable pageable);
}
