package uz.dizgo.erp.controller.logger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.logger.ProductActivityService;

import java.util.UUID;

@RestController
@RequestMapping("/api/product-activity-log")
@RequiredArgsConstructor
public class ProductActivityLogController {

    private final ProductActivityService service;
    private final ResponseEntityHelper helper;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductActivityLog(@PathVariable UUID productId,
                                                             @RequestParam(required = false) String activityType) {
        ApiResponse response = service.getProductActivityLog(productId, activityType);
        return helper.buildResponse(response);
    }
}
