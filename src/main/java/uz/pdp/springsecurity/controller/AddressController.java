package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.AddressDto;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.AddressRepository;
import uz.pdp.springsecurity.service.AddressService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AddressService addressService;

    /**
     * YANGI ADDRESS QO'SHISH
     *
     * @param addressDto
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("ADD_ADDRESS")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody AddressDto addressDto) {
        ApiResponse apiResponse = addressService.addAddress(addressDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ADDRESSNI TAXRIRLASH ID ORQALI
     * @param id
     * @param addressDto
     * @return ApiResponse(success - > true message - > EDITED)
     */
    @CheckPermission("EDIT_ADDRESS")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody AddressDto addressDto) {
        ApiResponse apiResponse = addressService.editAddress(id, addressDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ADDRESSNI ID ORQALI KO'RISH
     * @param id
     * @return ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_ADDRESS")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = addressService.getAddress(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * HAMMA ADDRESSLARNI OLIB CHIQISH
     * @return  ApiResponse(success - > true object - > value)
     */
    @CheckPermission("VIEW_ADDRESS")
    @GetMapping
    public HttpEntity<?> get() {
        ApiResponse apiResponse = addressService.getAddresses();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    /**
     * ADDRESSNI DELETE QILSIH
     * @param id
     * @return ApiResponse(success - > true message - > DELETED)
     */
    @CheckPermission("DELETE_ADDRESS")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = addressService.deleteAddress(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
