package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.PenaltyTemplate;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PenaltyTemplateDto;
import uz.pdp.springsecurity.repository.PenaltyTemplateRepository;

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
