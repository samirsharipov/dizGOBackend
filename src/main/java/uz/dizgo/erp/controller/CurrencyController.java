package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CurrencyDto;
import uz.dizgo.erp.service.CurrencyService;

import java.util.UUID;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    @GetMapping("/{businessId}")
    public HttpEntity<?> get(@PathVariable UUID businessId){
        ApiResponse response = currencyService.get(businessId);
        return ResponseEntity.status(response.isSuccess()? 200:409).body(response);
    }

    @PutMapping("/{businessId}")
    public HttpEntity<?> edit(@PathVariable UUID businessId, @RequestBody CurrencyDto currencyDto){
        ApiResponse response = currencyService.edit(businessId, currencyDto);
        return ResponseEntity.status(response.isSuccess()? 200:409).body(response);
    }
}
