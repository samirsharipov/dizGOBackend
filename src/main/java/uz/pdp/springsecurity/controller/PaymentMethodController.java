package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.PayMethodDto;
import uz.pdp.springsecurity.service.PayMethodService;

import java.util.UUID;

@RestController
@RequestMapping("/api/paymethod")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PayMethodService payMethodService;

    @CheckPermission("SUPER_ADMIN")
    @PostMapping("/add-super-admin-payment-method")
    public HttpEntity<?> addPaymentMethodSuperAdmin(@RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.addPaymentMethodSuperAdmin(payMethodDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_PAY_METHOD")
    @PostMapping
    public HttpEntity<?> add(@RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.add(payMethodDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_PAY_METHOD")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.edit(id, payMethodDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PAY_METHOD")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_PAY_METHOD")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_PAY_METHOD_ADMIN")
    @GetMapping("/get-by-business/{business_id}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID business_id) {
        ApiResponse apiResponse = payMethodService.getAllByBusiness(business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
