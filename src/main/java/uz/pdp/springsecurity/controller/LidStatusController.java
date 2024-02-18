package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.entity.LidStatus;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LidFieldDto;
import uz.pdp.springsecurity.payload.LidStatusDto;
import uz.pdp.springsecurity.payload.LidStatusPostDto;
import uz.pdp.springsecurity.service.LidStatusService;

import java.util.UUID;

@RestController
@RequestMapping("/api/lid-status")
@RequiredArgsConstructor
public class LidStatusController {
    private final LidStatusService service;


    @CheckPermission("VIEW_LID_STATUS")
    @GetMapping("/getByBusinessId/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_LID_STATUS")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_LID_STATUS")
    @PostMapping
    public HttpEntity<?> create(@RequestBody LidStatusPostDto lidStatusPostDto) {
        ApiResponse apiResponse = service.create(lidStatusPostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_LID_STATUS")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody LidStatusPostDto lidStatusPostDto) {
        ApiResponse apiResponse = service.edit(id, lidStatusPostDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_LID_STATUS")
    @PutMapping("change-lid-status-big/{id}")
    public HttpEntity<?> change(@PathVariable UUID id) {
        ApiResponse apiResponse = service.changeBig(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_LID_STATUS")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
