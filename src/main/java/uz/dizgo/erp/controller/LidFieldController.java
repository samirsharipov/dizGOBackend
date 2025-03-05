package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.LidFieldDto;
import uz.dizgo.erp.service.LidFieldService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lidField")
public class LidFieldController {

    private final LidFieldService lidFieldService;

    @CheckPermission("VIEW_FORM_LID")
    @GetMapping("/getByBusinessId/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = lidFieldService.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_FORM_LID")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = lidFieldService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_FORM_LID")
    @GetMapping("/valueTypeSelect/{businessId}")
    public HttpEntity<?> getOneById(@PathVariable UUID businessId) {
        ApiResponse apiResponse = lidFieldService.getOneById(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_FORM_LID")
    @PostMapping
    public HttpEntity<?> create(@RequestBody LidFieldDto lidFieldDto) {
        ApiResponse apiResponse = lidFieldService.create(lidFieldDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_FORM_LID")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody LidFieldDto lidFieldDto) {
        ApiResponse apiResponse = lidFieldService.edit(id, lidFieldDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_FORM_LID")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = lidFieldService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
