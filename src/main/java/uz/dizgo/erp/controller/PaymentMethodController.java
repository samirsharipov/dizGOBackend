package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.PayMethodDto;
import uz.dizgo.erp.service.PayMethodService;

import java.util.UUID;

@RestController
@RequestMapping("/api/paymethod")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PayMethodService payMethodService;
    private final ResponseEntityHelper responseEntityHelper;

    @CheckPermission("ADD_PAY_METHOD")
    @PostMapping
    public HttpEntity<?> add(@RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.add(payMethodDto);
        return responseEntityHelper.buildResponse(apiResponse);
    }

    @CheckPermission("EDIT_PAY_METHOD")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody PayMethodDto payMethodDto) {
        ApiResponse apiResponse = payMethodService.edit(id, payMethodDto);
        return responseEntityHelper.buildResponse(apiResponse);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> get(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.get(id);
        return responseEntityHelper.buildResponse(apiResponse);
    }

    @GetMapping
    public HttpEntity<ApiResponse> getAll() {
        return responseEntityHelper.buildResponse(payMethodService.getAll());
    }

    @CheckPermission("DELETE_PAY_METHOD")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse apiResponse = payMethodService.delete(id);
        return responseEntityHelper.buildResponse(apiResponse);
    }
}
