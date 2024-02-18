package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.InvoiceDto;
import uz.pdp.springsecurity.service.InvoiceService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @CheckPermission("EDIT_INVOICE")
    @PutMapping("/{branchId}")
    public HttpEntity<?> edit(@PathVariable UUID branchId, @Valid @RequestBody InvoiceDto invoiceDto) {
        ApiResponse apiResponse = invoiceService.edit(branchId, invoiceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{branchId}")
    public HttpEntity<?> getOne(@PathVariable UUID branchId) {
        ApiResponse apiResponse = invoiceService.getOne(branchId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
