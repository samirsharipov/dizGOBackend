package uz.dizgo.erp.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Reason;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ReasonDto;
import uz.dizgo.erp.repository.ReasonRepository;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public ApiResponse createReason(ReasonDto reasonDto) {
        Reason reason = mapToEntity(reasonDto);
        reasonRepository.save(reason);
        return new ApiResponse("Reason created successfully", true);
    }

    public ApiResponse getAllReasons(UUID businessId) {
        List<ReasonDto> all = reasonRepository.findByBusinessIdOrderByNameAsc(businessId);
        if (all.isEmpty())
            return new ApiResponse("No Reasons found for this business", false);

        return new ApiResponse("success", true, all);
    }

    public ApiResponse getReasonById(UUID id) {
        return reasonRepository.findById(id)
                .map(reason -> new ApiResponse("Reason found", true, mapToDto(reason)))
                .orElseGet(() -> new ApiResponse("Reason not found", false));
    }

    public ApiResponse updateReason(UUID id, ReasonDto reasonDto) {
        return reasonRepository.findById(id).map(reason -> {
            reason.setName(reasonDto.getName());
            reason.setBusinessId(reasonDto.getBusinessId());
            reasonRepository.save(reason);
            return new ApiResponse("Reason updated successfully", true);
        }).orElseGet(() -> new ApiResponse("Reason not found", false));
    }

    public ApiResponse deleteReason(UUID id) {
        if (!reasonRepository.existsById(id)) {
            return new ApiResponse("Reason not found", false);
        }
        reasonRepository.softDeleteById(id);
        return new ApiResponse("Reason deleted successfully", true);
    }

    private Reason mapToEntity(ReasonDto reasonDto) {
        return new Reason(reasonDto.getId(), reasonDto.getName(), reasonDto.getBusinessId());
    }

    private ReasonDto mapToDto(Reason reason) {
        return new ReasonDto(reason.getId(), reason.getName(), reason.getBusinessId());
    }
}