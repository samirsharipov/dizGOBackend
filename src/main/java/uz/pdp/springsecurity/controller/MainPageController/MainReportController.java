package uz.pdp.springsecurity.controller.MainPageController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.MainReportService;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequestMapping("/api/main-page")
@RequiredArgsConstructor
public class MainReportController {

    private final ResponseEntityHelper helper;
    private final MainReportService service;

    @GetMapping("get-main-report/{businessId}")
    public HttpEntity<ApiResponse> getMainReport(@PathVariable UUID businessId,
                                                 @RequestParam(required = false) UUID branchId,
                                                 @RequestParam(required = false) Timestamp startDate,
                                                 @RequestParam(required = false) Timestamp endDate) {
        return helper.buildResponse(service.getMainRepost(businessId, branchId, startDate, endDate));
    }
}
