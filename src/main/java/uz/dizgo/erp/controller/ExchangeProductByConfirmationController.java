package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.ConfirmationDto;
import uz.dizgo.erp.payload.ExchangeProductByConfirmationDto;
import uz.dizgo.erp.service.ExchangeProductByConfirmationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/exchangeProductByConfirmation")
@RequiredArgsConstructor
public class ExchangeProductByConfirmationController {

    private final ExchangeProductByConfirmationService service;

    @CheckPermission("ADD_EXCHANGE")
    @PostMapping
    public HttpEntity<?> add(@RequestBody ExchangeProductByConfirmationDto byConfirmationDto) {
        ApiResponse apiResponse = service.add(byConfirmationDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-by-id/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = service.get(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("VIEW_EXCHANGE")
    @GetMapping("/get-by-business-id/{businessId}")
    public HttpEntity<?> getByBusinessId(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.getByBusinessId(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_EXCHANGE")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ExchangeProductByConfirmationDto byConfirmationDto) {
        ApiResponse apiResponse = service.edit(id, byConfirmationDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("ADD_EXCHANGE")
    @PutMapping("/confirmation/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ConfirmationDto confirmationDto) {
        ApiResponse apiResponse = service.editConfirmation(id, confirmationDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}
