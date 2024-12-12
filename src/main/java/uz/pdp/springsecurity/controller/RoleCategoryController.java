package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.RoleCategoryDto;
import uz.pdp.springsecurity.service.RoleCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/role-category")
@RequiredArgsConstructor
public class RoleCategoryController {

    private final RoleCategoryService service;
    private final ResponseEntityHelper helper;


    @CheckPermission("ADD_ROLE")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleCategoryDto roleCategoryDto) {
        return helper.buildResponse(service.create(roleCategoryDto));
    }

    @CheckPermission("ADD_ROLE")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody RoleCategoryDto roleCategoryDto) {
        return helper.buildResponse(service.edit(id, roleCategoryDto));
    }

    @CheckPermission("GET_ROLE")
    @GetMapping("/get-by-business-id/{businessId}")
    public ResponseEntity<?> getByBusinessId(@PathVariable UUID businessId) {
        return helper.buildResponse(service.getRoleCategoriesByBusiness(businessId));
    }

    @CheckPermission("GET_ROLE")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return helper.buildResponse(service.getById(id));
    }
}