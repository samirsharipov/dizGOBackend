package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.RoleCategoryDto;
import uz.pdp.springsecurity.service.RoleCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/role-category")
@RequiredArgsConstructor
public class RoleCategoryController {

    private final RoleCategoryService service;


    @CheckPermission("ADD_ROLE")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleCategoryDto roleCategoryDto) {
        ApiResponse apiResponse = service.create(roleCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("ADD_ROLE")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody RoleCategoryDto roleCategoryDto) {
        ApiResponse apiResponse = service.edit(id, roleCategoryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @CheckPermission("GET_ROLE")
    @GetMapping("/get-by-business-id/{businessId}")
    public ResponseEntity<?> getByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @CheckPermission("GET_ROLE")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}