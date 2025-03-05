package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.DismissalDescription;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.DismissalDescriptionDto;
import uz.dizgo.erp.repository.BusinessRepository;
import uz.dizgo.erp.repository.DismissalDescriptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DismissalDescriptionService {

    private final DismissalDescriptionRepository descriptionRepository;
    private final BusinessRepository businessRepository;

    public ApiResponse create(DismissalDescriptionDto descriptionDto) {
        if (descriptionDto.getBusinessId() == null)
            return new ApiResponse("businessId is required", false);

        Optional<Business> optionalBusiness =
                businessRepository.findById(descriptionDto.getBusinessId());

        if (optionalBusiness.isEmpty())
            return new ApiResponse("businessId not found", false);

        descriptionRepository.save(fromDto(descriptionDto, new DismissalDescription(), optionalBusiness.get()));

        return new ApiResponse("success", true);
    }

    public ApiResponse edit(UUID id, DismissalDescriptionDto descriptionDto) {
        Optional<DismissalDescription> optional = descriptionRepository.findById(id);
        if (optional.isEmpty())
            return new ApiResponse("description not found", false);

        DismissalDescription dismissalDescription = optional.get();
        descriptionRepository.save(fromDto(descriptionDto, dismissalDescription, dismissalDescription.getBusiness()));

        return new ApiResponse("success", true);
    }

    public ApiResponse getByBusinessId(UUID businessId) {
        List<DismissalDescription> all =
                descriptionRepository.findAllByBusinessId(businessId);
        if (all.isEmpty())
            return new ApiResponse("description not found", false);

        return new ApiResponse("success", true, toDto(all));
    }

    public ApiResponse getById(UUID id) {
        Optional<DismissalDescription> optional = descriptionRepository.findById(id);
        return optional
                .map(dismissalDescription -> new ApiResponse("success", true,
                        toDto(new DismissalDescriptionDto(), dismissalDescription)))
                .orElseGet(() -> new ApiResponse("description not found", false));
    }

    public ApiResponse getDescriptionByBusinessIdIsMandatoryTrue(UUID businessId) {
        List<DismissalDescription> all =
                descriptionRepository.findAllByBusinessIdAndMandatoryTrue(businessId);
        if (all.isEmpty())
            return new ApiResponse("description not found", false);

        return new ApiResponse("success", true, toDto(all));
    }

    public ApiResponse getDescriptionByBusinessIdIsMandatoryFalse(UUID businessId) {
        List<DismissalDescription> all =
                descriptionRepository.findAllByBusinessIdAndMandatoryFalse(businessId);

        if (all.isEmpty())
            return new ApiResponse("description not found", false);

        return new ApiResponse("success", true, toDto(all));
    }


    private static DismissalDescription fromDto(DismissalDescriptionDto descriptionDto, DismissalDescription dismissalDescription, Business business) {
        dismissalDescription.setBusiness(business);
        dismissalDescription.setDescription(descriptionDto.getDescription());
        dismissalDescription.setMandatory(descriptionDto.isMandatory());
        return dismissalDescription;
    }

    private static DismissalDescriptionDto toDto(DismissalDescriptionDto descriptionDto, DismissalDescription dismissalDescription) {
        descriptionDto.setId(dismissalDescription.getId());
        descriptionDto.setDescription(dismissalDescription.getDescription());
        descriptionDto.setBusinessId(dismissalDescription.getBusiness().getId());
        descriptionDto.setMandatory(dismissalDescription.isMandatory());
        return descriptionDto;
    }

    private static List<DismissalDescriptionDto> toDto(List<DismissalDescription> dismissalDescriptions) {
        List<DismissalDescriptionDto> descriptionDtoList = new ArrayList<>();
        for (DismissalDescription dismissalDescription : dismissalDescriptions) {
            descriptionDtoList.add(toDto(new DismissalDescriptionDto(), dismissalDescription));
        }
        return descriptionDtoList;
    }
}
