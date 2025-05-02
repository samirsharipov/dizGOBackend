package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.AddressDto;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.AddressService;

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
        return buildResponse(addressService.createAddress(addressDto));
    }

    @CheckPermission("EDIT_ADDRESS")
    @PutMapping("/{id}")
    public HttpEntity<ApiResponse> edit(@PathVariable UUID id, @RequestBody AddressDto addressDto) {
        return buildResponse(addressService.updateAddress(id, addressDto));
    }

    @CheckPermission("VIEW_ADDRESS")
    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> get(@PathVariable UUID id) {
        return buildResponse(addressService.getAddress(id));
    }

    @GetMapping("/get-parent-is-null")
    public HttpEntity<ApiResponse> get() {
        return buildResponse(addressService.getAddresses());
    }

    @GetMapping("/get-parent-id/{parentId}")
    public HttpEntity<ApiResponse> getParentId(@PathVariable UUID parentId) {
        return buildResponse(addressService.getByParentId(parentId));
    }

    @GetMapping("/tree")
    public HttpEntity<ApiResponse> getAddressTree() {
        return buildResponse(addressService.getAddressTreeByBusiness());
    }

    @CheckPermission("DELETE_ADDRESS")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> delete(@PathVariable UUID id) {
        return buildResponse(addressService.deleteAddress(id));
    }

    private HttpEntity<ApiResponse> buildResponse(ApiResponse apiResponse) {
        return helper.buildResponse(apiResponse);
    }
}