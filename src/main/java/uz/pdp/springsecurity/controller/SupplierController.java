package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.RepaymentDto;
import uz.pdp.springsecurity.payload.SupplierDto;
import uz.pdp.springsecurity.service.SupplierService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_SUPPLIER")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody SupplierDto supplierDto) {
        return responseEntityHelper.buildResponse(supplierService.add(supplierDto));
    }

    @CheckPermission("EDIT_SUPPLIER")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody SupplierDto supplierDto) {
        return responseEntityHelper.buildResponse(supplierService.edit(id, supplierDto));
    }

    @CheckPermission("VIEW_SUPPLIER")
    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(supplierService.get(id));
    }

    @CheckPermission("DELETE_SUPPLIER")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(supplierService.delete(id));
    }

    @CheckPermission("VIEW_SUPPLIER")
    @GetMapping("/get-by-business/{businessId}")
    public HttpEntity<?> getAllByBusiness(@PathVariable UUID businessId, @RequestParam int page, @RequestParam int size) {
        return responseEntityHelper.buildResponse(supplierService.getAllByBusiness(businessId, page, size));
    }

    /**
     * DO'KON QARZINI TO'LASHI
     */
    @CheckPermission("ADD_SUPPLIER")
    @PostMapping("/repayment/{id}")
    public HttpEntity<?> addRepayment(@PathVariable UUID id, @RequestBody RepaymentDto repaymentDto) {
        return responseEntityHelper.buildResponse(supplierService.storeRepayment(id, repaymentDto));
    }


    @GetMapping("/history/{id}")
    public HttpEntity<?> supplierHistory(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(supplierService.supplierHistory(id));
    }

    /**
     * BIZNING DEALERLARDA QANCHA PULIMIZ BOR? YOKI QANCHA QARZIMIZ BOR?
     */
    @GetMapping("/ourMoney/{businessId}")
    public HttpEntity<?> ourMoney(@PathVariable UUID businessId) {
        return responseEntityHelper.buildResponse(supplierService.ourMoney(businessId));
    }
}