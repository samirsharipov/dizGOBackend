package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.BalanceHistoryService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance-history")
public class BalanceHistoryController {
    private final BalanceHistoryService service;


    @CheckPermission("VIEW_BALANCE_HISTORY")
    @GetMapping("/get-by-id/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BALANCE_HISTORY")
    @GetMapping("/get-by-all-balanceId/{balanceId}")
    public HttpEntity<?> getByBalanceId(@PathVariable UUID balanceId,
                                       @RequestParam int page, @RequestParam int size,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = service.getByBalanceId(balanceId, page, size, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BALANCE_HISTORY")
    @GetMapping("/get-by-all-branchId/{branchId}")
    public HttpEntity<?> getByBranchId(@PathVariable UUID branchId,
                                       @RequestParam int page, @RequestParam int size,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate) {
        ApiResponse apiResponse = service.getByBranchId(branchId, page, size, startDate, endDate);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
