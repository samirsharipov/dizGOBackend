package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.OutlayCategory;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.OutlayCategoryDto;
import uz.pdp.springsecurity.repository.BusinessRepository;
import uz.pdp.springsecurity.repository.OutlayCategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutlayCategoryService {

    private final OutlayCategoryRepository outlayCategoryRepository;
    private final BusinessRepository businessRepository;

    // Common method to get Business by Id
    private Business getBusinessById(UUID businessId) {
        return businessRepository.findById(businessId).orElse(null);
    }

    public ApiResponse add(OutlayCategoryDto outlayCategoryDto) {
        Business business = getBusinessById(outlayCategoryDto.getBusinessId());
        if (business == null) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }

        OutlayCategory outlayCategory = new OutlayCategory(outlayCategoryDto.getTitle(), business);
        outlayCategoryRepository.save(outlayCategory);
        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, OutlayCategoryDto outlayCategoryDto) {
        return outlayCategoryRepository.findById(id)
                .map(outlayCategory -> {
                    outlayCategory.setTitle(outlayCategoryDto.getTitle());
                    outlayCategoryRepository.save(outlayCategory);
                    return new ApiResponse("EDITED", true);
                })
                .orElse(new ApiResponse("NOT FOUND", false));
    }

    public ApiResponse get(UUID id) {
        return outlayCategoryRepository.findById(id)
                .map(outlayCategory -> new ApiResponse("FOUND", true, outlayCategory))
                .orElse(new ApiResponse("NOT FOUND", false));
    }

    public ApiResponse delete(UUID id) {
        if (outlayCategoryRepository.existsById(id)) {
            outlayCategoryRepository.deleteById(id);
            return new ApiResponse("DELETED", true);
        }
        return new ApiResponse("NOT FOUND", false);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {
        List<OutlayCategory> outlayCategories = outlayCategoryRepository.findAllByBusinessId(businessId);
        if (outlayCategories.isEmpty()) {
            return new ApiResponse("NOT FOUND", false);
        }
        return new ApiResponse("FOUND", true, outlayCategories);
    }
}