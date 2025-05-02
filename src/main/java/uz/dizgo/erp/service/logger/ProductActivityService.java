package uz.dizgo.erp.service.logger;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.ProductActivityLog;
import uz.dizgo.erp.entity.template.ProductActionType;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.logger.ProductActivityLogRepository;
import uz.dizgo.erp.service.MessageService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductActivityService {
    private final ProductActivityLogRepository repository;
    private final MessageService messageService;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    public ApiResponse getProductActivityLog(UUID productId, String activityType) {
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE, DEFAULT_SORT);

        if (isBlank(activityType)) {
            return buildSuccessResponse(repository.findAllByProductId(productId, pageable));
        }

        return parseActionType(activityType)
                .map(type -> repository.findAllByProductIdAndActionType(productId, type, pageable))
                .map(this::buildSuccessResponse)
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

    private ApiResponse buildSuccessResponse(Page<ProductActivityLog> logs) {
        return new ApiResponse(messageService.getMessage("found"), true, logs);
    }
}
