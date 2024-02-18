package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    int countAllByProjectId(UUID project_id);
    int countByTaskStatusId(UUID taskStatus_id);
    int countByProjectIdAndTaskStatus_OrginalName(UUID project_id, String taskStatus_orginalName);
    int countByProjectId(UUID project_id);
    int countAllByTaskStatus_OrginalNameAndProjectId(String taskStatus_orginalName, UUID project_id);
    int countAllByProjectIdAndExpiredTrue(UUID project_id);
    int countAllByStageId(UUID stage_id);
    int countAllByStageIdAndTaskStatus_OrginalName(UUID stage_id, String taskStatus_orginalName);
    Page<Task> findAllByBranch_Id(UUID branch_id, Pageable pageable);
    Page<Task> findAllByTaskPriceList_UserList_Id(UUID userId, Pageable pageable);
    Page<Task> findAllByProjectIdAndTaskStatusIdAndTaskTypeIdAndExpiredTrue(UUID project_id, UUID taskStatus_id, UUID taskType_id, Pageable pageable);
    List<Task> findAllByProjectId(UUID project_id);
    Page<Task> findAllByProject_Id(UUID project_id,Pageable pageable);
    Page<Task> findAllByBranch_IdAndTaskPriceList_UserList_Id(UUID branch_id, UUID taskPriceList_userList_id, Pageable pageable);
    List<Task> findAllByBranchId(UUID branch_id);
    List<Task> findAllByBranch_BusinessId(UUID branch_id);
    Page<Task> findByNameContainingIgnoreCaseAndBranchId(String name, UUID branch_id, Pageable pageable);
    Page<Task> findByNameContainingIgnoreCaseAndBranchIdAndTaskPriceList_UserList_Id(String name, UUID branch_id, UUID taskPriceList_userList_id, Pageable pageable);
    Page<Task> findAllByTaskStatus_Id(UUID project_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProjectIdAndTaskTypeIdAndExpiredTrue(UUID taskStatus_id, UUID project_id, UUID taskType_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProjectIdAndTaskTypeIdAndExpiredTrueAndTaskPriceList_UserList_Id(UUID taskStatus_id, UUID project_id, UUID taskType_id, UUID taskPriceList_userList_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProjectIdAndTaskTypeId(UUID id, UUID projectId, UUID typeId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProjectIdAndTaskTypeIdAndTaskPriceList_UserList_Id(UUID id, UUID projectId, UUID typeId,UUID userId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProjectIdAndExpiredTrue(UUID id, UUID projectId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProject_Id(UUID id, UUID projectId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndTaskTypeId(UUID id, UUID typeId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndExpiredTrue(UUID id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndTaskTypeIdAndExpiredTrue(UUID id, UUID typeId, Pageable pageable);

    Page<Task> findAllByProjectIdAndTaskTypeIdAndExpiredTrue(UUID projectId, UUID typeId, Pageable pageable);

    Page<Task> findAllByTaskTypeId(UUID taskType_id, Pageable pageable);

    Page<Task> findAllByBranch_IdAndExpiredTrue(UUID branch_id, Pageable pageable);

    Page<Task> findAllByTaskStatusId(UUID statusId, Pageable pageable);

    Page<Task> findAllByProjectIdAndExpiredTrue(UUID projectId, Pageable pageable);


    @Query("SELECT COUNT(t) FROM Task t JOIN t.taskStatus ts JOIN t.taskPriceList tp JOIN tp.userList u WHERE ts.orginalName = :taskStatusOriginalName AND u.id = :userId")
    int countTasksByTaskStatusOriginalNameAndUserId(@Param("taskStatusOriginalName") String taskStatusOriginalName, @Param("userId") UUID userId);


    @Query("SELECT COUNT(t) FROM Task t JOIN t.taskPriceList tp JOIN tp.userList u WHERE t.expired = true AND u.id = :userId")
    int countExpiredTasksByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(t) FROM Task t JOIN t.taskPriceList tp JOIN tp.userList u WHERE u.id = :userId")
    List<Task> findAllByTasksByUserId(@Param("userId") UUID userId);

    @Query("SELECT t FROM Task t JOIN t.taskPriceList tp JOIN tp.userList u WHERE u.id = :userId")
    List<Task> findTasksByUserId(@Param("userId") UUID userId);

    Page<Task> findAllByTaskStatusIdAndProjectIdAndExpiredTrueAndTaskPriceList_UserList_Id(UUID taskStatus_id, UUID project_id, UUID taskPriceList_userList_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndTaskTypeIdAndExpiredTrueAndTaskPriceList_UserList_Id(UUID id, UUID typeId,UUID userId, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndProject_IdAndTaskPriceList_UserList_Id(UUID taskStatus_id, UUID project_id, UUID taskPriceList_userList_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndTaskTypeIdAndTaskPriceList_UserList_Id(UUID taskStatus_id, UUID taskType_id, UUID taskPriceList_userList_id, Pageable pageable);
    Page<Task> findAllByTaskStatusIdAndExpiredTrueAndTaskPriceList_UserList_Id(UUID id,UUID userId, Pageable pageable);
    Page<Task> findAllByTaskStatus_IdAndTaskPriceList_UserList_Id(UUID id,UUID userId, Pageable pageable);
}
