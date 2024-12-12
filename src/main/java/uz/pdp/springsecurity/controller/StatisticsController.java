package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.StatisticsService;

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
}