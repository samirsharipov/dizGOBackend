package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.NavigationDto;
import uz.dizgo.erp.service.NavigationService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/navigation")
@RequiredArgsConstructor
public class NavigationController {
    private final NavigationService navigationService;

    @CheckPermission("ADD_NAVIGATION")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody NavigationDto navigationDto) {
        ApiResponse apiResponse = navigationService.add(navigationDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_NAVIGATION")
    @GetMapping("/{branchId}")
    public HttpEntity<?> getOne(@PathVariable UUID branchId) {
        ApiResponse apiResponse = navigationService.getOne(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_NAVIGATION")
    @GetMapping("/averageSell/{branchId}")
    public HttpEntity<?> getAverageSell(@PathVariable UUID branchId) {
        ApiResponse apiResponse = navigationService.getAverageSell(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_NAVIGATION")
    @DeleteMapping("/{branchId}")
    public HttpEntity<?> delete(@PathVariable UUID branchId) {
        ApiResponse apiResponse = navigationService.delete(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
