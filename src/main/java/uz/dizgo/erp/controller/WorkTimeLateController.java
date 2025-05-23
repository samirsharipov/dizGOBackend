package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.WorkTimeLateService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/workTimeLate")
@RequiredArgsConstructor
public class WorkTimeLateController {
    private final WorkTimeLateService workTimeLateService;
    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/by-user/{userId}")
    public HttpEntity<?> getByUser(@PathVariable UUID userId, @RequestParam() UUID branchId) {
        ApiResponse apiResponse = workTimeLateService.getByUser(userId, branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_WORK_TIME")
    @GetMapping("/by-branch/{branchId}")
    public HttpEntity<?> getByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = workTimeLateService.getByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
