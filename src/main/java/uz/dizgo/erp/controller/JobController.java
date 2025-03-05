package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.JobDto;
import uz.dizgo.erp.service.JobService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService service;

    @CheckPermission("VIEW_JOB")
    @GetMapping("/getByBusinessId/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_JOB")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_JOB")
    @PostMapping
    public HttpEntity<?> create(@RequestBody JobDto jobDto) {
        ApiResponse apiResponse = service.create(jobDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_JOB")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody JobDto jobDto) {
        ApiResponse apiResponse = service.edit(id, jobDto);
    return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_JOB")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
