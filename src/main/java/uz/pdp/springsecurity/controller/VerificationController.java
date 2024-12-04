package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.service.VerificationCodeService;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationCodeService verificationCodeService;
    private final ResponseEntityHelper responseEntityHelper;

    @PostMapping
    public HttpEntity<ApiResponse> sendVerificationCode(@RequestParam("phoneNumber") String phoneNumber,boolean refresh) {
        return responseEntityHelper.buildResponse(verificationCodeService.sendVerificationCode(phoneNumber,refresh));
    }

    @GetMapping
    public HttpEntity<?> getVerificationCode(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("code") String code) {
        boolean verifyCode = verificationCodeService.verifyCode(phoneNumber, code);
        if (verifyCode) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(409).build();
    }
}
