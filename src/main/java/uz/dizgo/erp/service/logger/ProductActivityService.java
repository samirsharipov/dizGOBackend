package uz.dizgo.erp.service.logger;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.ProductActivityLog;
import uz.dizgo.erp.entity.template.ProductActionType;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.projections.ProductActivityLogDto;
import uz.dizgo.erp.repository.logger.ProductActivityLogRepository;
import uz.dizgo.erp.service.MessageService;
import uz.dizgo.erp.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductActivityService {
    private final ProductActivityLogRepository repository;
    private final MessageService messageService;
    private final UserService userService;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    public ApiResponse getProductActivityLog(UUID productId, String activityType, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);

        if (isBlank(activityType)) {
            return buildSuccessResponse(dtoList(repository.findAllByProductId(productId, pageable), pageable));
        }

        return parseActionType(activityType)
                .map(type -> repository.findAllByProductIdAndActionType(productId, type, pageable))
                .map(logs -> buildSuccessResponse(dtoList(logs, pageable)))
                .orElseGet(() -> new ApiResponse("Noto‘g‘ri actionType qiymati: " + activityType, false));
    }

    private Optional<ProductActionType> parseActionType(String type) {
        try {
            return Optional.of(ProductActionType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private ApiResponse buildSuccessResponse(Page<ProductActivityLogDto> logs) {
        return new ApiResponse(messageService.getMessage("found"), true, logs);
    }

    private Page<ProductActivityLogDto> dtoList(Page<ProductActivityLog> logs, Pageable pageable) {
        List<ProductActivityLogDto> dtoList = logs.stream().map(log -> {
            String fullName = Optional.ofNullable(log.getCreatedBy())
                    .flatMap(userService::getUserById)
                    .map(user -> user.getFirstName() + " " + user.getLastName())
                    .orElse(null);

            return new ProductActivityLogDto(
                    log.getCreatedAt(),
                    fullName,
                    log.getActionType().name(),
                    log.getOldData(),
                    log.getNewData(),
                    log.getExtraData()
            );
        }).toList();

        return new PageImpl<>(dtoList, pageable, logs.getTotalElements());
    }
}
