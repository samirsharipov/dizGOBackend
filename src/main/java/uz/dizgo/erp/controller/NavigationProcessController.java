package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.NavigationProcessService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/navigationProcess")
@RequiredArgsConstructor
public class NavigationProcessController {
    private final NavigationProcessService navigationProcessService;

    @CheckPermission("VIEW_NAVIGATION")
    @GetMapping("/{branchId}")
    public HttpEntity<?> get(@PathVariable UUID branchId,
                             @RequestParam() Integer year,
                             @RequestParam() Integer month) {
        ApiResponse apiResponse = navigationProcessService.get(branchId, year, month);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_NAVIGATION")
    @GetMapping("/update/{branchId}")
    public HttpEntity<?> update(@PathVariable UUID branchId) {
        ApiResponse apiResponse = navigationProcessService.update(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
