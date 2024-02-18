package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.WaitingDTO;
import uz.pdp.springsecurity.service.WaitingService;

import java.util.UUID;

@RestController
@RequestMapping("/api/waiting")
@RequiredArgsConstructor
public class WaitingController {
   private final WaitingService waitingService;

    @CheckPermission("ADD_TRADE")
    @PostMapping
    public HttpEntity<?> create(@RequestBody WaitingDTO waitingDTO) {
        ApiResponse apiResponse = waitingService.create(waitingDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_TRADE")
    @GetMapping("/{branchId}")
    public HttpEntity<?> get(@PathVariable UUID branchId) {
        ApiResponse apiResponse = waitingService.get(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_TRADE")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = waitingService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}