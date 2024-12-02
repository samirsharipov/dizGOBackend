package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.AddressDto;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.AddressService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final ResponseEntityHelper helper;

    @CheckPermission("ADD_ADDRESS")
    @PostMapping
    public HttpEntity<ApiResponse> add(@Valid @RequestBody AddressDto addressDto) {
        return helper.buildResponse(addressService.createAddress(addressDto));
    }

    @CheckPermission("EDIT_ADDRESS")
    @PutMapping("/{id}")
    public HttpEntity<ApiResponse> edit(@PathVariable UUID id, @RequestBody AddressDto addressDto) {
        return helper.buildResponse(addressService.updateAddress(id, addressDto));
    }

    @CheckPermission("VIEW_ADDRESS")
    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> get(@PathVariable UUID id) {
        return helper.buildResponse(addressService.getAddress(id));
    }

    @CheckPermission("VIEW_ADDRESS")
    @GetMapping("/get-parent-is-null")
    public HttpEntity<ApiResponse> get() {
        return helper.buildResponse(addressService.getAddresses());
    }

    @CheckPermission("VIEW_ADDRESS")
    @GetMapping("/get-parent-id/{parentId}")
    public HttpEntity<ApiResponse> getParentId(@PathVariable UUID parentId) {
        return helper.buildResponse(addressService.getByParentId(parentId));
    }


    @CheckPermission("DELETE_ADDRESS")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> delete(@PathVariable UUID id) {
        return helper.buildResponse(addressService.deleteAddress(id));
    }
}