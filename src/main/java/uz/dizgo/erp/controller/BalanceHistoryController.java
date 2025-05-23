package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.BalanceHistoryService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance-history")
public class BalanceHistoryController {

    private final BalanceHistoryService service;

    @GetMapping("/get-by-id/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-all-balanceId/{balanceId}")
    public HttpEntity<?> getByBalanceId(@PathVariable UUID balanceId,
                                        @RequestParam int page, @RequestParam int size,
                                        @RequestParam(required = false) Date startDate,
                                        @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = service.getByBalanceId(balanceId, page, size, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-all-branchId/{branchId}")
    public HttpEntity<?> getByBranchId(@PathVariable UUID branchId,
                                       @RequestParam int page, @RequestParam int size,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = service.getByBranchId(branchId, page, size, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
