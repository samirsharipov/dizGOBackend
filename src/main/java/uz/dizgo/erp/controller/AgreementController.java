package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
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

    @CheckPermission("EDIT_SALARY")
    @PutMapping("/{userId}")
    public HttpEntity<?> edit(@PathVariable UUID userId, @Valid @RequestBody AgreementGetDto agreementGetDto) {
        ApiResponse apiResponse = agreementService.edit(userId, agreementGetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission("GET_SALARY")
    @GetMapping("/{userId}")
    public HttpEntity<?> getOne(@PathVariable UUID userId) {
        ApiResponse apiResponse = agreementService.getOne(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
