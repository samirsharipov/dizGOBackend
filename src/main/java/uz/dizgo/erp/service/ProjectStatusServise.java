package uz.dizgo.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.ProjectStatus;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ProjectStatusDto;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.ProjectStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectStatusServise {

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    ProjectStatusRepository projectStatusRepository;
    public ApiResponse add(ProjectStatusDto projectStatusDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(projectStatusDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        ProjectStatus projectStatus=new ProjectStatus();
        projectStatus.setName(projectStatusDto.getName());
        projectStatus.setBranch(optionalBranch.get());
        projectStatusRepository.save(projectStatus);
        return new ApiResponse("Added",true,projectStatus);
    }

    public ApiResponse edit(UUID id, ProjectStatusDto projectStatusDto) {
        boolean exists = projectStatusRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        ProjectStatus projectStatus = projectStatusRepository.getById(id);
        projectStatus.setName(projectStatusDto.getName());
        ProjectStatus projectStatuses = projectStatusRepository.save(projectStatus);
        return new ApiResponse("Edited",true,projectStatuses);
    }

    public ApiResponse get(UUID id) {
        boolean exists = projectStatusRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        ProjectStatus projectStatus = projectStatusRepository.getById(id);
        return new ApiResponse("Found",true,projectStatus);
    }

    public ApiResponse delete(UUID id) {
        boolean exists = projectStatusRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        projectStatusRepository.deleteById(id);
        return new ApiResponse("Deleted",true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        List<ProjectStatus> projectStatusList = projectStatusRepository.findAllByBranchId(branchId);
        if (projectStatusList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,projectStatusList);
    }
}
