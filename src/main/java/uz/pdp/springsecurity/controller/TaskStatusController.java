package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.TaskStatusDto;
import uz.pdp.springsecurity.repository.TaskStatusRepository;
import uz.pdp.springsecurity.service.TaskStatusServise;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task/status")
public class TaskStatusController {

    @Autowired
    TaskStatusServise taskStatusServise;

    @CheckPermission("ADD_TASK_STATUS")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TaskStatusDto taskStatusDto) {
        ApiResponse apiResponse = taskStatusServise.add(taskStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_TASK_STATUS")
    @PutMapping("/{id}/{branchId}")
    public HttpEntity<?> edit(@PathVariable UUID id,
                              @PathVariable UUID branchId,
                              @RequestBody TaskStatusDto taskStatusDto) {
        ApiResponse apiResponse = taskStatusServise.edit(id,branchId,taskStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission("GET_TASK_STATUS")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = taskStatusServise.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_TASK_STATUS")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = taskStatusServise.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @CheckPermission("GET_TASK_STATUS")
    @GetMapping("/get-by-branchId/{branchId}")
    public HttpEntity<?> getAllByBranch(@PathVariable UUID branchId) {
        ApiResponse apiResponse = taskStatusServise.getAllByBranch(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
