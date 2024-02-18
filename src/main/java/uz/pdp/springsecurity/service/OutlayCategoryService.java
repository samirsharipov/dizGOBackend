package uz.pdp.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.OutlayCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.OutlayCategoryDto;
import uz.pdp.springsecurity.repository.BranchRepository;
import uz.pdp.springsecurity.repository.OutlayCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OutlayCategoryService {
    @Autowired
    OutlayCategoryRepository outlayCategoryRepository;

    @Autowired
    BranchRepository branchRepository;

    public ApiResponse add(OutlayCategoryDto outlayCategoryDto) {

        Optional<Branch> optionalBranch = branchRepository.findById(outlayCategoryDto.getBranchId());
        if (optionalBranch.isEmpty()) {
            return new ApiResponse("BRANCH NOT FOUND", false);
        }

        OutlayCategory outlayCategory = new OutlayCategory(
                outlayCategoryDto.getTitle(),
                optionalBranch.get()
        );

        outlayCategoryRepository.save(outlayCategory);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, OutlayCategoryDto outlayCategoryDto) {
        if (!outlayCategoryRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);

        OutlayCategory outlayCategory = outlayCategoryRepository.getById(id);
        outlayCategory.setTitle(outlayCategoryDto.getTitle());

        outlayCategoryRepository.save(outlayCategory);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!outlayCategoryRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, outlayCategoryRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        if (!outlayCategoryRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        outlayCategoryRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAllByBranchId(UUID branch_id) {
        List<OutlayCategory> allByBranch_id = outlayCategoryRepository.findAllByBranch_Id(branch_id);
        if (allByBranch_id.isEmpty()) return new ApiResponse("NOT FOUND",false);
        return new ApiResponse("FOUND",true,allByBranch_id);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<OutlayCategory> allByBusinessId = outlayCategoryRepository.findAllByBranchBusinessId(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND",false);
        return new ApiResponse("FOUND",true,allByBusinessId);
    }
}
