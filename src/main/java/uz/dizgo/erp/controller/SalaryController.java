package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.SalaryDto;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.SalaryService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @CheckPermission("EDIT_SALARY")
    @PutMapping("/pay-salary/{salaryId}")
    public HttpEntity<?> paySalary(@PathVariable UUID salaryId, @Valid @RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.paySalary(salaryId, salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_SALARY")
    @PutMapping("/pay-avans/{salaryId}")
    public HttpEntity<?> payAvans(@PathVariable UUID salaryId, @Valid @RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.payAvans(salaryId, salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_SALARY")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> getAll(@PathVariable UUID branchId) {
        ApiResponse apiResponse = salaryService.getAll(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("GET_SALARY")
    @GetMapping("/by-user/{userId}")
    public HttpEntity<?> getAllByUser(@PathVariable UUID userId, @RequestParam() UUID branchId) {
        ApiResponse apiResponse = salaryService.getAllByUser(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("GET_SALARY")
    @GetMapping("/by-user-last-month/{userId}")
    public HttpEntity<?> getByUserLast(@PathVariable UUID userId, @RequestParam() UUID branchId) {
        ApiResponse apiResponse = salaryService.getByUserLast(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_SALARY")
    @GetMapping("/{salaryId}")
    public HttpEntity<?> getOne(@PathVariable UUID salaryId) {
        ApiResponse apiResponse = salaryService.getOne(salaryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
