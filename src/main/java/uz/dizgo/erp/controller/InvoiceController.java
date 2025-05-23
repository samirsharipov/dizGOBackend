package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.InvoiceDto;
import uz.dizgo.erp.service.InvoiceService;

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
