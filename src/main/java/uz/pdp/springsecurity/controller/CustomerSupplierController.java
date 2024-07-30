package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CustomerSupplierDto;
import uz.pdp.springsecurity.service.CustomerSupplierService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/customer-supplier")
@RequiredArgsConstructor
public class CustomerSupplierController {
    private final CustomerSupplierService customerSupplierService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerSupplierDto customerSupplierDto) {
        ApiResponse apiResponse = customerSupplierService.create(customerSupplierDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<?> getAll(@PathVariable UUID businessId,
                                    @RequestParam int size,
                                    @RequestParam int page) {
        ApiResponse apiResponse = customerSupplierService.getAll(businessId,size,page);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
