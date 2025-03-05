package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.TradeProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/trade/product")
@RequiredArgsConstructor
public class TradeProductController {

    private final TradeProductService tradeProductService;

    @CheckPermission("VIEW_MY_TRADE")
    @GetMapping("/get-all-trade/{businessId}")
    public HttpEntity<?> getAllByTrader(@PathVariable UUID businessId) {
        ApiResponse apiResponse = tradeProductService.getAllTrade(businessId);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
