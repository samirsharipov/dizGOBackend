package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.VerificationCode;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsSendService smsService;

    public ApiResponse sendVerificationCode(String phoneNumber) {
        Optional<VerificationCode> existingCode = verificationCodeRepository
                .findByPhoneNumberAndVerifiedFalse(phoneNumber);

        if (existingCode.isPresent()) {
            if (existingCode.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                return new ApiResponse("A verification code has already been sent. Please wait for it to expire.", false);
            }

            verificationCodeRepository.delete(existingCode.get());
        }

        // Yangi tasdiqlash kodini yaratish
        String code = generateCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // 5 daqiqa amal qiladi
        verificationCodeRepository.save(verificationCode);

        // SMS xizmatidan kodni yuborish
        smsService.sendVerificationCode(phoneNumber, code);

        return new ApiResponse("Verification code sent", true);
    }

    public boolean verifyCode(String phoneNumber, String code) {

        Optional<VerificationCode> optionalCode = verificationCodeRepository
                .findByPhoneNumberAndCodeAndVerifiedFalse(phoneNumber, code);

        if (optionalCode.isPresent() && optionalCode.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            VerificationCode verificationCode = optionalCode.get();
            verificationCode.setVerified(true);
            verificationCodeRepository.save(verificationCode);
            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(999999)).substring(0, 6);
    }
}
