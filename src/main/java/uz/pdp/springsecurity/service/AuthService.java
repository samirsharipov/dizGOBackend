package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.security.JwtProvider;
import uz.pdp.springsecurity.utils.Constants;


@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final VerificationCodeService verificationCodeService;
    private final JwtProvider jwtProvider;
    private final SmsSendService smsService;
    private final MessageService messageService;

    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageService.getMessage("error.message")));
    }

    // Tasdiqlash kodi bilan tekshiruv
    public ApiResponse verifyCodeForSuperAdmin(String code) {
        if (verificationCodeService.verifyCodeForSuperAdmin(code)) {
            User principal = userRepository.findByRoleName(Constants.SUPER_ADMIN).orElse(null);
            if (principal != null) {
                String token = jwtProvider.generateToken(principal.getUsername(), principal.getRole());
                sendInfoForSuperAdmin();
                return new ApiResponse(token, true, principal);
            }
        }
        return new ApiResponse("Incorrect verification code", false);
    }

    // Yangi tasdiqlash kodini yuborish
    public ApiResponse refreshVerificationCodes() {
        if (sendVerificationCodeForSuperAdmin("998977677793").isSuccess()
                && sendVerificationCodeForSuperAdmin("998908051040").isSuccess()
                && sendVerificationCodeForSuperAdmin("998770440105").isSuccess()
        ) {
            return new ApiResponse("Verification codes sent", true);
        }
        return new ApiResponse("Verification codes not sent", false);
    }

    public ApiResponse sendVerificationCodeForSuperAdmin(String phoneNumber) {
        return verificationCodeService.sendVerificationCode(phoneNumber, false, true);
    }

    public void sendInfoForSuperAdmin() {
        String[] phoneNumbers = {"998977677793", "998908051040", "998770440105"};
        for (String phoneNumber : phoneNumbers) {
            smsService.sendInfoSuperAdminMessage(phoneNumber);
        }
    }
}
