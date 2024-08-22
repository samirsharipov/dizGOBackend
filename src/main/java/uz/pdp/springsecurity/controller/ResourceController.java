package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ResourceDto;
import uz.pdp.springsecurity.service.ResourceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<?> createResource(@RequestBody ResourceDto resourceDto) {
        ApiResponse apiResponse = resourceService.create(resourceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-branch/{branchId}")
    public ResponseEntity<?> getByBranchId(@PathVariable UUID branchId) {
        ApiResponse apiResponse = resourceService.getByBranchId(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-business/{businessId}")
    public ResponseEntity<?> getResourceByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = resourceService.getByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PatchMapping("/refresh-by-branch/{branchId}")
    public ResponseEntity<?> refreshByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = resourceService.refreshByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PatchMapping("/refresh-by-business/{businessId}")
    public ResponseEntity<?> refreshByBusiness(@PathVariable UUID businessId) {
        ApiResponse apiResponse = resourceService.refreshByBusiness(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



}
