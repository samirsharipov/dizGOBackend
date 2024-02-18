package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TaskStatusDto;
import uz.pdp.springsecurity.payload.TaskTypeDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.TaskTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskTypeServise {

    @Autowired
    TaskTypeRepository taskTypeRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BranchRepository branchRepository;

    public ApiResponse add(TaskTypeDto taskTypeDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(taskTypeDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        TaskType taskType = new TaskType();
        taskType.setName(taskTypeDto.getName());
        taskType.setBranch(optionalBranch.get());
        taskTypeRepository.save(taskType);
        return new ApiResponse("Added",true,taskType);
    }

    public ApiResponse edit(UUID id, TaskTypeDto taskTypeDto) {
        boolean exists = taskTypeRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        TaskType taskType = taskTypeRepository.getById(id);
        taskType.setName(taskTypeDto.getName());
        TaskType status = taskTypeRepository.save(taskType);
        return new ApiResponse("Edited",true,status);
    }

    public ApiResponse get(UUID id) {
        Optional<TaskType> optionalTaskType = taskTypeRepository.findById(id);
        return optionalTaskType.map(taskType -> new ApiResponse("Found", true, taskType)).orElseGet(() -> new ApiResponse("Not Found"));
    }

    public ApiResponse delete(UUID id) {
        boolean exists = taskTypeRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        taskTypeRepository.deleteById(id);
        return new ApiResponse("Deleted",true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        List<TaskType> taskTypeList = taskTypeRepository.findAllByBranchId(branchId);
        if (taskTypeList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,taskTypeList);
    }

    public ApiResponse getAllBranchPageable(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<TaskType> taskTypes = taskTypeRepository.findAllByBranch_Id(branchId,pageable);
        if (taskTypes.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,taskTypes);
    }


}
