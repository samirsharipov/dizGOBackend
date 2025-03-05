package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.RoleDto;
import uz.dizgo.erp.service.RoleService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @CheckPermission("ADD_ROLE")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody RoleDto roleDto) {
        ApiResponse apiResponse = roleService.add(roleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_ROLE")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @Valid @RequestBody RoleDto roleDto) {
        ApiResponse apiResponse = roleService.edit(id, roleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = roleService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID business_id) {
        ApiResponse apiResponse = roleService.getAllByBusiness(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_ROLE")
    @GetMapping("/get-by-business-role/{business_id}")
    public HttpEntity<?> getByBusinessRole(@PathVariable UUID business_id) {
        ApiResponse apiResponse = roleService.getByBusinessRole(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-role-permissions/{business_id}")
    public HttpEntity<?> getRolePermissions(@PathVariable UUID business_id) {
        ApiResponse apiResponse = roleService.getRolePermissions(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-role-tariff/{tariff_id}")
    public HttpEntity<?> getRoleByTariff(@PathVariable UUID tariff_id) {
        ApiResponse apiResponse = roleService.getRoleByTariff(tariff_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-role-category-id/{roleCategoryId}")
    public HttpEntity<?> getRoleByRoleCategory(@PathVariable UUID roleCategoryId) {
        ApiResponse apiResponse = roleService.getRoleByRoleCategory(roleCategoryId);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
