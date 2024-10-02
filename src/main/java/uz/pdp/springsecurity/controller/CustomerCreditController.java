package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.CreditRepaymentDto;
import uz.pdp.springsecurity.service.CustomerCreditService;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer-credit")
@RequiredArgsConstructor
public class CustomerCreditController {

    private final CustomerCreditService service;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerCredit(@PathVariable UUID customerId) {
        ApiResponse apiResponse = service.getCustomerCredit(customerId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/repayment/{customerCreditId}}")
    public ResponseEntity<?> updateCustomerCredit(@PathVariable UUID customerCreditId, @RequestBody CreditRepaymentDto repaymentDto) {
        ApiResponse apiResponse = service.repayment(customerCreditId,repaymentDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getCustomerCreditById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
