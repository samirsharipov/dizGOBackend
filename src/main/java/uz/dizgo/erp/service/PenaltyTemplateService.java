package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.PenaltyTemplate;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.PenaltyTemplateDto;
import uz.dizgo.erp.repository.PenaltyTemplateRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PenaltyTemplateService {

    private final PenaltyTemplateRepository penaltyTemplateRepository;
    private final MessageService messageService;

    public ApiResponse create(PenaltyTemplateDto penaltyTemplateDto) {
        PenaltyTemplate penaltyTemplate = new PenaltyTemplate();
        penaltyTemplate.setName(penaltyTemplateDto.getName());
        penaltyTemplate.setBranchId(penaltyTemplateDto.getBranchId());
        penaltyTemplate.setPerMinutePenalty(penaltyTemplateDto.getPerMinutePenalty());
        penaltyTemplateRepository.save(penaltyTemplate);

        return new ApiResponse(messageService.getMessage("added.successfully"), true);
    }


    public ApiResponse edit(UUID id, PenaltyTemplateDto penaltyTemplateDto) {

        penaltyTemplateRepository.findById(id)
                .ifPresent(penaltyTemplate -> {
                    penaltyTemplate.setName(penaltyTemplateDto.getName());
                    penaltyTemplate.setBranchId(penaltyTemplateDto.getBranchId());
                    penaltyTemplate.setPerMinutePenalty(penaltyTemplateDto.getPerMinutePenalty());
                    penaltyTemplateRepository.save(penaltyTemplate);
                });

        return new ApiResponse(messageService.getMessage("edited.successfully"), true);
    }

    public ApiResponse get(UUID branchId) {
        List<PenaltyTemplate> all = penaltyTemplateRepository.findAllByBranchId(branchId);
        if (all.isEmpty())
            return new ApiResponse(messageService.getMessage("not.found"), false);

        return new ApiResponse(messageService.getMessage("found"), true, all);
    }
}
