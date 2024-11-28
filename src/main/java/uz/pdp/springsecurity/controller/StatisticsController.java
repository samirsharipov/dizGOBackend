package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.StatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stat")
public class StatisticsController {

    private final StatisticsService statisticsService;


    @GetMapping("/business")
    public HttpEntity<ApiResponse> businessStatistics() {
        ApiResponse response = statisticsService.businessStatistics();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
