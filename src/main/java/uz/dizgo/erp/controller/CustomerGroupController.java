package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.CustomerGroupDto;
import uz.dizgo.erp.service.CustomerGroupService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/customerGroup")
@RequiredArgsConstructor
public class CustomerGroupController {

    private final CustomerGroupService customerGroupService;

    @CheckPermission("ADD_CUSTOMER_GROUP")
    @PostMapping
    public HttpEntity<?> addCustomerGroup(@Valid @RequestBody CustomerGroupDto customerGroupDto) {
        ApiResponse apiResponse = customerGroupService.addCustomerGroup(customerGroupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_CUSTOMER_GROUP")
    @GetMapping("/get/{businessId}")
    public HttpEntity<?> getAll(@PathVariable UUID businessId) {
        ApiResponse apiResponse = customerGroupService.getAll(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_CUSTOMER_GROUP")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteCustomerGroup(@PathVariable UUID id) {
        ApiResponse apiResponse = customerGroupService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_CUSTOMER_GROUP")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = customerGroupService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody CustomerGroupDto customerGroupDto) {
        ApiResponse apiResponse = customerGroupService.edit(id, customerGroupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
