package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.StatisticsService;

import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stat")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ResponseEntityHelper responseEntityHelper;


    @GetMapping("/business")
    public HttpEntity<ApiResponse> businessStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.businessStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/product")
    public HttpEntity<ApiResponse> productStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.productStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/trade")
    public HttpEntity<ApiResponse> tradeStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.tradeStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/user")
    public HttpEntity<ApiResponse> userStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.userStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/customer")
    public HttpEntity<ApiResponse> customerStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.customerStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/supplier")
    public HttpEntity<ApiResponse> supplierStatistics(@RequestParam String type) {
        ApiResponse response = statisticsService.supplierStatistics(type);
        return responseEntityHelper.buildResponse(response);
    }

    @GetMapping("/attendance/{userId}")
    public HttpEntity<ApiResponse> userStatisticsInfo(@PathVariable UUID userId, @RequestParam Timestamp startDate, @RequestParam Timestamp endDate) {
        return responseEntityHelper.buildResponse(statisticsService.userStatisticsInfo(userId, startDate, endDate));
    }
}