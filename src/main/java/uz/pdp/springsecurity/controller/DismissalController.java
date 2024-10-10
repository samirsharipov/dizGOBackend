package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.DismissalDto;
import uz.pdp.springsecurity.service.DismissalService;

import java.util.UUID;

@RestController
@RequestMapping("/api/dismissal")
@RequiredArgsConstructor
public class DismissalController {

    private final DismissalService service;

    @CheckPermission("ADD_USER")
    @PostMapping("/create")
    public ResponseEntity<?> createDismissal(@RequestBody DismissalDto dismissalDto) {
        ApiResponse apiResponse = service.create(dismissalDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getDismissalById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-dismissal-users/{businessId}")
    public ResponseEntity<?> getDismissalUsers(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getDismissalUsers(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }
}
