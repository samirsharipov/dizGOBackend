package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.AgreementGetDto;
import uz.dizgo.erp.service.AgreementService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;
    private final ResponseEntityHelper helper;

    @CheckPermission("EDIT_SALARY")
    @PutMapping("/{userId}")
    public HttpEntity<?> edit(@PathVariable UUID userId, @Valid @RequestBody AgreementGetDto agreementGetDto) {
        return buildResponse(agreementService.edit(userId, agreementGetDto));
    }

    @CheckPermission("GET_SALARY")
    @GetMapping("/{userId}")
    public HttpEntity<?> getOne(@PathVariable UUID userId) {
        return buildResponse(agreementService.getOne(userId));
    }

    private HttpEntity<ApiResponse> buildResponse(ApiResponse apiResponse) {
        return helper.buildResponse(apiResponse);
    }
}
