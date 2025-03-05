package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.VerificationCode;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.VerificationCodeRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private static final Logger log = LoggerFactory.getLogger(VerificationCodeService.class);
    private final SmsSendService smsService;


    @Transactional
    public ApiResponse sendVerificationCode(String phoneNumber, boolean refresh, boolean isSuperAdmin) {
        // Yangi tasdiqlash kodi yaratish
        VerificationCode existingCode = verificationCodeRepository.findByPhoneNumber(phoneNumber).orElse(null);

        String code = generateCode();
        if (existingCode != null) {
            // Agar telefon raqami mavjud bo'lsa, uni yangilash

            if (!isSuperAdmin && existingCode.getExpiresAt().isAfter(LocalDateTime.now())) {
                return new ApiResponse("Verification code already exists and is still valid", false);
            }

            existingCode.setCode(code);
            existingCode.setExpiresAt(LocalDateTime.now().plusMinutes(3));
            existingCode.setSuperAdmin(isSuperAdmin);
        } else {
            // Agar telefon raqami mavjud bo'lmasa, yangi yozuv qo'shish
            existingCode = new VerificationCode(phoneNumber, code, LocalDateTime.now().plusMinutes(3), isSuperAdmin);
        }
        existingCode.setVerified(false);
        verificationCodeRepository.save(existingCode);

        // SMS xizmatidan kodni yuborish (asinxron)
        CompletableFuture.runAsync(() -> {
            try {
                smsService.sendVerificationCode(phoneNumber, code);
                log.info("Verification code sent to {}", phoneNumber);
            } catch (Exception e) {
                log.error("SMS sending failed: {}", e.getMessage());
            }
        });

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

    public boolean verifyCodeForSuperAdmin(String code) {

        Optional<VerificationCode> optionalCode = verificationCodeRepository
                .findByCodeAndSuperAdminTrue(code);

        if (optionalCode.isPresent() && optionalCode.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            VerificationCode verificationCode = optionalCode.get();
            verificationCode.setVerified(true);
            verificationCodeRepository.save(verificationCode);
            return true;
        }
        return false;
    }

    public boolean verifyCode(String phoneNumber) {
        Optional<VerificationCode> optional = verificationCodeRepository.findByPhoneNumber(phoneNumber);
        if (optional.isEmpty()) {
            return false;
        }
        VerificationCode verificationCode = optional.get();
        if (verificationCode.getExpiresAt().isAfter(LocalDateTime.now())) {
            return false;
        }
        return verificationCode.isVerified();
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
