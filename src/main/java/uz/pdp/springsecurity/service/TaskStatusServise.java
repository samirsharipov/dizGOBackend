package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Task;
import uz.pdp.springsecurity.entity.TaskStatus;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TaskStatusDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.TaskRepository;
import uz.pdp.springsecurity.repository.TaskStatusRepository;

import java.util.*;

@Service
public class TaskStatusServise {

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    TaskRepository taskRepository;

    public ApiResponse add(TaskStatusDto taskStatusDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(taskStatusDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setABoolean(taskStatusDto.isABoolean());
        long ordinalNumber = taskStatusRepository.countByBranchId(optionalBranch.get().getId());
        TaskStatus completed = taskStatusRepository.getByOrginalNameAndBranchId("Completed",optionalBranch.get().getId());
        completed.setRowNumber(ordinalNumber+1);
        taskStatusRepository.save(completed);
        taskStatus.setRowNumber(ordinalNumber);
        taskStatus.setName(taskStatusDto.getName());
        taskStatus.setColor(taskStatusDto.getColor());
        taskStatus.setBranch(optionalBranch.get());
        taskStatusRepository.save(taskStatus);
        return new ApiResponse("Added",true,taskStatus);
    }

    public ApiResponse edit(UUID id, UUID branchId, TaskStatusDto taskStatusDto) {
        boolean exists = taskStatusRepository.existsById(id);
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        Branch branch = optionalBranch.get();
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        TaskStatus taskStatus = taskStatusRepository.getById(id);
        taskStatus.setABoolean(taskStatusDto.isABoolean());
        updateTaskStatusOrdinalNumber(taskStatus,taskStatusDto.getRowNumber(),branch.getId());
        taskStatus.setName(taskStatusDto.getName());
        taskStatus.setColor(taskStatusDto.getColor());
        TaskStatus status = taskStatusRepository.save(taskStatus);
        return new ApiResponse("Edited",true,status);
    }

    public final void updateTaskStatusOrdinalNumber(TaskStatus taskStatus, long newOrdinalNumber, UUID branchId) {
        long currentOrdinalNumber = taskStatus.getRowNumber();
        if (currentOrdinalNumber == newOrdinalNumber) {
            return;
        }

        List<TaskStatus> allTaskStatuses = taskStatusRepository.findAllByBranchIdOrderByRowNumber(branchId);
        if (newOrdinalNumber > currentOrdinalNumber) {
            for (TaskStatus ts : allTaskStatuses) {
                if (ts.getRowNumber() > currentOrdinalNumber && ts.getRowNumber() <= newOrdinalNumber) {
                    ts.setRowNumber(ts.getRowNumber() - 1);
                    taskStatusRepository.save(ts);
                }
            }
        } else {
            for (TaskStatus ts : allTaskStatuses) {
                if (ts.getRowNumber() >= newOrdinalNumber && ts.getRowNumber() < currentOrdinalNumber) {
                    ts.setRowNumber(ts.getRowNumber() + 1);
                    taskStatusRepository.save(ts);
                }
            }
        }
        taskStatus.setRowNumber(newOrdinalNumber);

        taskStatusRepository.save(taskStatus);
    }

    public ApiResponse get(UUID id) {
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(id);
        return optionalTaskStatus.map(taskStatus -> new ApiResponse("Found", true, taskStatus)).orElseGet(() -> new ApiResponse("Not Found"));
    }

    public ApiResponse delete(UUID id) {
        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(id);
        if (optionalTaskStatus.isPresent()) {
            TaskStatus taskStatusToDelete = optionalTaskStatus.get();
            if (taskStatusToDelete.getOrginalName() != null){
                return new ApiResponse("You can not deleteAll this task status !",false);
            }
            Branch branch = taskStatusToDelete.getBranch();

            taskStatusRepository.delete(taskStatusToDelete);


            List<TaskStatus> allTaskStatuses = taskStatusRepository.findAllByBranchIdOrderByRowNumber(branch.getId());
            int index = 1;
            for (TaskStatus ts : allTaskStatuses) {
                if (ts.getId() != taskStatusToDelete.getId()) {
                    ts.setRowNumber(index++);
                    taskStatusRepository.save(ts);
                }
            }
        }
        return new ApiResponse("Deleted",true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        List<TaskStatus> taskStatusList = taskStatusRepository.findAllByBranchId(branchId);
        if (taskStatusList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        for (TaskStatus taskStatus : taskStatusList) {
            int count = taskRepository.countByTaskStatusId(taskStatus.getId());
            taskStatus.setNumberOfTask(count);
        }
        taskStatusList.sort(Comparator.comparing(TaskStatus::getRowNumber));
        return new ApiResponse("Found",true,taskStatusList);
    }
}
