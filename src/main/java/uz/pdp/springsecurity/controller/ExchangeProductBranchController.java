package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.ExchangeProductBranchDTO;
import uz.pdp.springsecurity.service.ExchangeProductBranchService;

import java.sql.Date;
import java.util.UUID;


@RestController
@RequestMapping("/api/exchange-product-branch")
@RequiredArgsConstructor
public class ExchangeProductBranchController {

    private final ExchangeProductBranchService exchangeProductBranchService;

    @CheckPermission("ADD_EXCHANGE")
    @PostMapping
    public HttpEntity<?> create(@RequestBody ExchangeProductBranchDTO exchangeProductBranchDTO) {
        ApiResponse apiResponse = exchangeProductBranchService.create(exchangeProductBranchDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


//    @CheckPermission("EDIT_EXCHANGE")
//    @PutMapping("/{id}")
//    public HttpEntity<?> paySalary(@PathVariable UUID id, @RequestBody ExchangeProductBranchDTO exchangeProductBranchDTO) {
//        ApiResponse apiResponse = exchangeProductBranchService.paySalary(id, exchangeProductBranchDTO);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
//    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = exchangeProductBranchService.getOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_EXCHANGE")
    @DeleteMapping("/{id}")
    public HttpEntity<ApiResponse> deleteOne(@PathVariable UUID id) {
        ApiResponse apiResponse = exchangeProductBranchService.deleteOne(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-byDate/{exchangeDate}/{business_id}")
    public HttpEntity<?> getByDate(@PathVariable Date exchangeDate, @PathVariable UUID business_id) {
        ApiResponse apiResponse = exchangeProductBranchService.getByDate(exchangeDate, business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-by-statusId/{exchangeStatus_id}/{business_id}")
    public HttpEntity<?> getByExchangeStatus(@PathVariable UUID exchangeStatus_id, @PathVariable UUID business_id) {
        ApiResponse apiResponse = exchangeProductBranchService.getByStatusId(exchangeStatus_id, business_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE_ADMIN")
    @GetMapping("/get-by-businessId/{businessId}")
    public HttpEntity<?> getByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = exchangeProductBranchService.getByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-by-shipped-branch/{shippedBranch_id}")
    public HttpEntity<?> getByShippedBranchId(@PathVariable UUID shippedBranch_id) {
        ApiResponse apiResponse = exchangeProductBranchService.getByShippedBranchId(shippedBranch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-by-received-branch/{receivedBranch_id}")
    public HttpEntity<?> getByReceivedBranchId(@PathVariable UUID receivedBranch_id) {
        ApiResponse apiResponse = exchangeProductBranchService.getByReceivedBranchId(receivedBranch_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}