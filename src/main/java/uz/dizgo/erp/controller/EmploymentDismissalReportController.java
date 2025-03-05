package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.EmploymentDismissalReportService;

import java.util.UUID;

@RestController
@RequestMapping("/api/employment-dismissal-report")
@RequiredArgsConstructor
public class EmploymentDismissalReportController {

    private final EmploymentDismissalReportService service;

    @GetMapping("/get-info-only-count/{businessId}")
    public ResponseEntity<?> getInfoOnlyCount(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getInfoOnlyCount(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-between-year-count-info/{businessId}")
    public ResponseEntity<?> getBetweenYearCount(@PathVariable UUID businessId,
                                                 @RequestParam(required = false) int startYear,
                                                 @RequestParam(required = false) int endYear) {
        ApiResponse apiResponse = service.getBetweenYearCount(businessId, startYear,endYear);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
