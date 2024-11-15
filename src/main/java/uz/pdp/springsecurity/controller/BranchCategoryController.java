package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BranchCategoryDto;
import uz.pdp.springsecurity.service.BranchCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/branch-category")
@RequiredArgsConstructor
public class BranchCategoryController {
    private final BranchCategoryService service;

    @CheckPermission("ADD_BRANCH_CATEGORY")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody BranchCategoryDto branchCategoryDto) {
        ApiResponse apiResponse = service.create(branchCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("GET_BRANCH_CATEGORY")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> get(@PathVariable UUID id) {
        ApiResponse apiResponse = service.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @CheckPermission("GET_BRANCH_CATEGORY")
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        ApiResponse apiResponse = service.getAll();
        return ResponseEntity.ok(apiResponse);
    }

    @CheckPermission("PUT_BRANCH_CATEGORY")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> edit(@PathVariable UUID id, @RequestBody BranchCategoryDto branchCategoryDto) {
        ApiResponse apiResponse = service.edit(id, branchCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @CheckPermission("DELETE_BRANCH_CATEGORY")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}