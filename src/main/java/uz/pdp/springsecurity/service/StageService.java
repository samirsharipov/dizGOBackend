package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Stage;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.StageDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.StageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StageService {

    @Autowired
    StageRepository stageRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BranchRepository branchRepository;

    public ApiResponse add(StageDto stageDto) {
        Optional<Branch> optionalBranch = branchRepository.findById(stageDto.getBranchId());
        if (optionalBranch.isEmpty()){
            return new ApiResponse("Branch Not Found",false);
        }
        Stage stage= new Stage();
        stage.setName(stageDto.getName());
        stage.setBranch(optionalBranch.get());
        stageRepository.save(stage);
        return new ApiResponse("Added",true,stage);
    }

    public ApiResponse edit(UUID id, StageDto stageDto) {
        boolean exists = stageRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        Stage stage= stageRepository.getById(id);
        stage.setName(stageDto.getName());
        Stage status = stageRepository.save(stage);
        return new ApiResponse("Edited",true,status);
    }

    public ApiResponse get(UUID id) {
        Optional<Stage> optionalStage = stageRepository.findById(id);
        return optionalStage.map(stage -> new ApiResponse("Found", true, stage)).orElseGet(() -> new ApiResponse("Not Found"));
    }

    public ApiResponse delete(UUID id) {
        boolean exists = stageRepository.existsById(id);
        if (!exists){
            return new ApiResponse("Not Found",false);
        }
        stageRepository.deleteById(id);
        return new ApiResponse("Deleted",true);
    }

    public ApiResponse getAllByBranch(UUID businessId) {
        List<Stage> stageList = stageRepository.findAllByBranchId(businessId);
        if (stageList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,stageList);
    }

    public ApiResponse getAllByBranchPageable(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Stage> stageList = stageRepository.findAllByBranch_Id(branchId,pageable);
        if (stageList.isEmpty()){
            return new ApiResponse("Not Found",false);
        }
        return new ApiResponse("Found",true,stageList);
    }
}
