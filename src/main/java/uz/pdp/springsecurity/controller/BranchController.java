package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BranchDto;
import uz.pdp.springsecurity.service.BranchService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @CheckPermission("ADD_BRANCH")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody BranchDto branchDto) {
        ApiResponse apiResponse = branchService.addBranch(branchDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BRANCH")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody BranchDto branchDto) {
        ApiResponse apiResponse = branchService.editBranch(id, branchDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_BRANCH")
    @PutMapping("edit-main-branch/{businessId}")
    public HttpEntity<?> editMainBranch(@PathVariable UUID businessId, @RequestParam UUID branchCategoryId) {
        ApiResponse apiResponse = branchService.editMainBranch(businessId, branchCategoryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BRANCH")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = branchService.getBranch(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_BRANCH")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = branchService.deleteBranch(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_BRANCH")
    @GetMapping("get-all-by-business-id/{business_id}")
    public HttpEntity<?> getByBusinessId(@PathVariable UUID business_id) {
        ApiResponse apiResponse = branchService.getByBusinessId(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
