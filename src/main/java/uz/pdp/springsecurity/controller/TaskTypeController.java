package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TaskStatusDto;
import uz.pdp.springsecurity.payload.TaskTypeDto;
import uz.pdp.springsecurity.service.TaskStatusServise;
import uz.pdp.springsecurity.service.TaskTypeServise;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task/type")
public class TaskTypeController {

    @Autowired
    TaskTypeServise taskTypeServise;

    @CheckPermission("ADD_TASK_TYPE")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TaskTypeDto taskTypeDto) {
        ApiResponse apiResponse = taskTypeServise.add(taskTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_TASK_TYPE")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody TaskTypeDto taskTypeDto) {
        ApiResponse apiResponse = taskTypeServise.edit(id,taskTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_TASK_TYPE")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = taskTypeServise.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_TASK_TYPE")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = taskTypeServise.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_TASK_TYPE")
    @GetMapping("/get-by-branch/{branchId}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID branchId) {
        ApiResponse apiResponse = taskTypeServise.getAllByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_TASK_TYPE")
    @GetMapping("/get-by-branchPageable/{branchId}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID branchId,
                                          @RequestParam(defaultValue = "0", required = false) int page,
                                          @RequestParam(defaultValue = "10", required = false) int size) {
        ApiResponse apiResponse = taskTypeServise.getAllBranchPageable(branchId,page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
