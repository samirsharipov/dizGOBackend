package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.ProjectType;
import uz.pdp.springsecurity.entity.Stage;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ProjectTypeDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.ProjectRepository;
import uz.pdp.springsecurity.repository.ProjectTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectTypeServise {

    @Autowired
    ProjectTypeRepository projectTypeRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    public ApiResponse add(ProjectTypeDto projectTypeDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(projectTypeDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        ProjectType projectType=new ProjectType();
        projectType.setName(projectTypeDto.getName());
        projectType.setBranch(optionalBranch.get());
        projectTypeRepository.save(projectType);
        return new ApiResponse("Added",true,projectType);
    }

    public ApiResponse edit(UUID id, ProjectTypeDto projectTypeDto) {
        boolean exists = projectTypeRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        ProjectType projectTypes = projectTypeRepository.getById(id);
        projectTypes.setName(projectTypeDto.getName());
        ProjectType projectType = projectTypeRepository.save(projectTypes);
        return new ApiResponse("Edited",true,projectType);
    }

    public ApiResponse get(UUID id) {
        Optional<ProjectType> optionalProjectType = projectTypeRepository.findById(id);
        return optionalProjectType.map(projectType -> new ApiResponse("Found", true, projectType)).orElseGet(() -> new ApiResponse("Not Found"));
    }

    public ApiResponse delete(UUID id) {
        boolean exists = projectTypeRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        boolean existsType = projectRepository.existsByProjectTypeId(id);
        if (existsType){
            return new ApiResponse("This project type is connected to another project !",false);
        }
        projectTypeRepository.deleteById(id);
        return new ApiResponse("Deleted",true);
    }

    public ApiResponse getAllByBranch(UUID branchId) {
        List<ProjectType> projectTypeList = projectTypeRepository.findAllByBranchId(branchId);
        if (projectTypeList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,projectTypeList);
    }

    public ApiResponse getAllByBranchPageable(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ProjectType> projectTypes = projectTypeRepository.findAllByBranch_Id(branchId,pageable);
        if (projectTypes.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,projectTypes);
    }
}
