package uz.pdp.springsecurity.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.WorkTimePostDto;
import uz.pdp.springsecurity.service.WorkTimeService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/workTime")
@RequiredArgsConstructor
public class WorkTimeController {
    private final WorkTimeService workTimeService;

    @CheckPermission("ADD_WORK_TIME")
    @PostMapping("/arrive")
    public HttpEntity<?> arrive(@RequestBody WorkTimePostDto workTimePostDto) {
        ApiResponse apiResponse = workTimeService.arrive(workTimePostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_WORK_TIME")
    @PutMapping("/leave")
    public HttpEntity<?> leave(@RequestBody WorkTimePostDto workTimePostDto) {
        ApiResponse apiResponse = workTimeService.leave(workTimePostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/by-user-last-month/{userId}")
    public HttpEntity<?> getByUserLastMonth(@PathVariable UUID userId, @RequestParam() UUID branchId) {
        ApiResponse apiResponse = workTimeService.getByUserLastMonth(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/by-users-on-work/{branchId}")
    public HttpEntity<?> getOnWork(@PathVariable UUID branchId) {
        ApiResponse apiResponse = workTimeService.getOnWork(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/by-branch-come-work/{branchId}")
    public HttpEntity<?> getComeWork(@PathVariable UUID branchId, @RequestParam(required = false) Month selectedMonth, @RequestParam(required = false) Integer selectedYear) {
        if (selectedMonth == null) {
            selectedMonth = LocalDate.now().getMonth();
        }
        if (selectedYear == null) {
            selectedYear = LocalDate.now().getYear();
        }
        ApiResponse apiResponse = workTimeService.getComeWork(branchId, selectedMonth, selectedYear);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
//    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/getAllMonths/{branchId}")
    public HttpEntity<?> getMonth(@PathVariable UUID branchId) {
        ApiResponse apiResponse = workTimeService.getMonthsByYear(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @PutMapping("/previousAttendances")
    public HttpEntity<?> testPreviousAttendances() {
        ApiResponse apiResponse = workTimeService.testPreviousAttendances();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
