package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.DismissalDescriptionDto;
import uz.pdp.springsecurity.service.DismissalDescriptionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/description")
@RequiredArgsConstructor
public class DismissalDescriptionController {

    private final DismissalDescriptionService service;

    @CheckPermission("ADD_USER")
    @PostMapping("/create")
    public ResponseEntity<?> createDescription(@RequestBody DismissalDescriptionDto descriptionDto) {
        ApiResponse apiResponse = service.create(descriptionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @CheckPermission("ADD_USER")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editDescription(@PathVariable UUID id, @RequestBody DismissalDescriptionDto descriptionDto) {
        ApiResponse apiResponse = service.edit(id, descriptionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getDescriptionById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-business-id/{businessId}")
    public ResponseEntity<?> getDescriptionByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-business-id-is-mandatory-true/{businessId}")
    public ResponseEntity<?> getDescriptionByBusinessIdIsMandatoryTrue(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getDescriptionByBusinessIdIsMandatoryTrue(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_USER")
    @GetMapping("/get-by-business-id-is-mandatory-false/{businessId}")
    public ResponseEntity<?> getDescriptionByBusinessIdIsMandatoryFalse(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getDescriptionByBusinessIdIsMandatoryFalse(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
