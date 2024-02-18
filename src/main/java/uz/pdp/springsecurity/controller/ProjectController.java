package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ProjectDto;
import uz.pdp.springsecurity.service.ProjectService;
import uz.pdp.springsecurity.utils.AppConstant;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @CheckPermission("ADD_PROJECT")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody ProjectDto projectDto) {
        ApiResponse apiResponse = projectService.add(projectDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT', 'EDIT_PROJECT')")
    @PutMapping("/patch/{projectId}/{statusId}")
    public HttpEntity<?> updateProjectStatus(@PathVariable UUID projectId,
                                             @PathVariable UUID statusId) {
        ApiResponse apiResponse = projectService.updateProjectStatus(projectId, statusId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_PROJECT")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ProjectDto projectDto) {
        ApiResponse apiResponse = projectService.edit(id, projectDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_PROJECT")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = projectService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/get-one/{id}")
    public HttpEntity<?> getOne(@PathVariable UUID id) {
        ApiResponse apiResponse = projectService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PROJECT")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = projectService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/get-by-branch-sorted/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId,
                                        @RequestParam(required = false) UUID userId,
                                        @RequestParam(required = false) UUID typeId,
                                        @RequestParam(required = false) UUID customerId,
                                        @RequestParam(required = false) UUID projectStatusId,
                                        @RequestParam(required = false) Date expired,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = AppConstant.DEFAULT_SIZE) int size) {
        ApiResponse apiResponse = projectService.getAllByBranchId(branchId, userId,  typeId, customerId, projectStatusId, expired, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/get-by-branchId/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = projectService.getAllByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_OWN_PROJECT")
    @GetMapping("/get-by-own-branchId/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId,
                                        @RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = "10", required = false) int size,
                                        @RequestParam(required = false) UUID userId) {
        ApiResponse apiResponse = projectService.getOwnProject(branchId, userId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("GET_PROJECT")
    @GetMapping("/get-by-name/{name}/{branchId}")
    public HttpEntity<?> getAllByName(
            @PathVariable String name,
            @PathVariable UUID branchId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        ApiResponse apiResponse = projectService.searchByName(name, branchId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize(value = "hasAnyAuthority('GET_PROJECT', 'GET_OWN_PROJECT')")
    @GetMapping("/progress/{projectId}")
    public HttpEntity<?> getProgress(@PathVariable UUID projectId) {
        ApiResponse apiResponse = projectService.getProgress(projectId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
