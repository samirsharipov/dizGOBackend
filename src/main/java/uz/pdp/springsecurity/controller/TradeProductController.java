package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.TradeProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/trade/product")
public class TradeProductController {

    @Autowired
    TradeProductService tradeProductService;

    @CheckPermission("VIEW_MY_TRADE")
    @GetMapping("/get-all-trade/{businessId}")
    public HttpEntity<?> getAllByTrader(@PathVariable UUID businessId) {
        ApiResponse apiResponse = tradeProductService.getAllTrade(businessId);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
