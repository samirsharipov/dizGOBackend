package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.TariffDto;
import uz.dizgo.erp.service.TariffService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tariff")
public class TariffController {

    private final TariffService service;

    @CheckPermission("GET_TARIFF")
    @GetMapping
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = service.getAll();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getToChooseATariff")
    public HttpEntity<?> getToChooseATariff() {
        ApiResponse apiResponse = service.getToChooseATariff();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getById/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = service.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("CREATE_TARIFF")
    @PostMapping
    public HttpEntity<?> create(@RequestBody TariffDto tariffDto) {
        ApiResponse apiResponse = service.create(tariffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("EDIT_TARIFF")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody TariffDto tariffDto) {
        ApiResponse apiResponse = service.edit(id, tariffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("DELETE_TARIFF")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = service.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/businessInfo/{businessId}")
    public HttpEntity<?> businessInfo(@PathVariable UUID businessId) {
        ApiResponse apiResponse = service.businessInfo(businessId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
