package uz.pdp.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public HttpEntity<ApiResponse> sendVerificationCode(@RequestParam("phoneNumber") String phoneNumber) {
        return responseEntityHelper.buildResponse(verificationCodeService.sendVerificationCode(phoneNumber));
    }
}
