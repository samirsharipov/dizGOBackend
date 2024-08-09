package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.ExchangeStatusService;


@RequestMapping("/api/exchange/status")
@RestController
@RequiredArgsConstructor
public class ExchangeStatusController {

    private final ExchangeStatusService exchangeStatusService;

    @GetMapping("/getAllStatus")
    public HttpEntity<?> getAllStatus() {
        ApiResponse apiResponse = exchangeStatusService.getAllStatus();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
