package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BalancePostDto;
import uz.pdp.springsecurity.service.BalanceService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance")
public class BalanceController {
    private final BalanceService balanceService;

    @PutMapping("/{branchId}")
    public HttpEntity<?> edit(@PathVariable UUID branchId, @RequestBody BalancePostDto balancePostDto) {
        ApiResponse apiResponse = balanceService.edit(branchId, balancePostDto.getSumma(), true, balancePostDto.getPayMethodId(), balancePostDto.isDollar(), balancePostDto.getDescription());
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{branchId}")
    public HttpEntity<?> getAll(@PathVariable UUID branchId) {
        ApiResponse apiResponse = balanceService.getAll(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/business/{businessId}")
    public HttpEntity<?> getAllByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = balanceService.getAllByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-balance/{businessId}")
    public HttpEntity<?> getBalance(@PathVariable UUID businessId, @RequestParam(required = false) UUID branchId) {
        ApiResponse apiResponse = balanceService.getBalance(businessId,branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
