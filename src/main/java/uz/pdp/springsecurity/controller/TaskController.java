package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LostProductionDto;
import uz.pdp.springsecurity.payload.TaskDto;
import uz.pdp.springsecurity.service.TaskServise;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServise taskServise;

    @CheckPermission("ADD_TASK")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskServise.add(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_TASK")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskServise.edit(id,taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @PutMapping("/edit/{id}/{statusId}")
    public HttpEntity<?> updateTaskStatus(@PathVariable UUID id,
                                          @PathVariable  UUID statusId) {
        ApiResponse apiResponse = taskServise.updateTaskStatus(id,statusId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @PutMapping("/edit-invalid-amount-production/{taskId}")
    public HttpEntity<?> editInvalid(@PathVariable UUID taskId,
                                          @RequestBody LostProductionDto lostProductionDto) {
        ApiResponse apiResponse = taskServise.editInvalid(taskId, lostProductionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @PutMapping("change/{statusId}")
    public HttpEntity<?> updateTaskStatus(@PathVariable  UUID statusId,
                                          @RequestParam boolean isIncrease) {
        ApiResponse apiResponse = taskServise.updateTaskStatusIncrease(statusId,isIncrease);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = taskServise.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_TASK")
    @GetMapping("/branch/")
    public HttpEntity<?> getAll(@RequestParam( required = false) UUID branchId,
                                @RequestParam( required = false) UUID userId) {
        ApiResponse apiResponse = taskServise.getAll(branchId,userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_OWN_TASK")
    @GetMapping("/branch/{branchId}")
    public HttpEntity<?> getOwnTask(@PathVariable UUID branchId,
                                    @RequestParam(required = false) UUID userId,
                                    @RequestParam(defaultValue = "0", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = taskServise.getOwnTask(userId,branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_TASK")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = taskServise.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @GetMapping("/get-by-branch/")
    public HttpEntity<?> getAllByBranch(@RequestParam(required = false) UUID branchId,
                                        @RequestParam(required = false) UUID projectId,
                                        @RequestParam(required = false) UUID statusId,
                                        @RequestParam(required = false) UUID typeId,
                                        @RequestParam(required = false) UUID userId,
                                        @RequestParam(required = false) Date expired,
                                        @RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = taskServise.getAllByBranchId(branchId,projectId,statusId,typeId,userId,expired,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_TASK")
    @GetMapping("/get-by-project/{projectId}")
    public HttpEntity<?> getAllByProject(@PathVariable UUID projectId) {
        ApiResponse apiResponse = taskServise.getAllByProjectId(projectId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @CheckPermission("GET_TASK")
    @GetMapping("/get-by-name/{name}")
    public HttpEntity<?> getAllByName(
            @RequestParam(required = false) UUID branchId,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) UUID userId,
            @PathVariable String name) {
        ApiResponse apiResponse = taskServise.searchByName(branchId,name,page,size,userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PreAuthorize("hasAnyAuthority('GET_TASK', 'GET_OWN_TASK')")
    @GetMapping("/get-by-branch-pageable/")
    public HttpEntity<?> getAllByBranchPageable(@RequestParam(required = false) UUID branchId,
                                                @RequestParam(required = false) UUID projectId,
                                                @RequestParam(required = false) UUID typeId,
                                                @RequestParam(required = false) UUID userId,
                                                @RequestParam(required = false) Date expired,
                                                @RequestParam(required = false) Map<String,String> params) {
        ApiResponse apiResponse = taskServise.getAllByBranchIdPageable(branchId,params,projectId,typeId,userId,expired);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_TASK")
    @PostMapping("/duplicate-task/{taskId}")
    public HttpEntity<?> duplicateTask(@PathVariable UUID taskId) {
        ApiResponse apiResponse = taskServise.duplicateTask(taskId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
