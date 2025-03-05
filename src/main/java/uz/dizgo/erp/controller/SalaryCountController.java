package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.SalaryCountDto;
import uz.dizgo.erp.service.SalaryCountService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/salaryCount")
@RequiredArgsConstructor
public class SalaryCountController {
    private final SalaryCountService salaryCountService;

    @CheckPermission("CREATE_SALARY")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody SalaryCountDto salaryCountDto) {
        ApiResponse apiResponse = salaryCountService.add(salaryCountDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission("GET_SALARY")
    @GetMapping("/by-user-last-month/{userId}")
    public HttpEntity<?> getByUserLastMonth(@PathVariable UUID userId, @RequestParam() UUID branchId) {
        ApiResponse apiResponse = salaryCountService.getByUserLastMonth(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_SALARY")
    @GetMapping("/{salaryCountId}")
    public HttpEntity<?> getOne(@PathVariable UUID salaryCountId) {
        ApiResponse apiResponse = salaryCountService.getOne(salaryCountId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
