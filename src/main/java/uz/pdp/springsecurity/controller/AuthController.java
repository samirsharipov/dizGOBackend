package uz.pdp.springsecurity.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.helpers.ResponseEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LoginDto;
import uz.pdp.springsecurity.repository.UserRepository;
import uz.pdp.springsecurity.security.JwtProvider;
import uz.pdp.springsecurity.service.AuthService;
import uz.pdp.springsecurity.service.SalaryCountService;
import uz.pdp.springsecurity.service.VerificationCodeService;
import uz.pdp.springsecurity.utils.Constants;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final SalaryCountService salaryCountService;
    private final VerificationCodeService verificationCodeService;
    private final AuthService authService;
    private final ResponseEntityHelper responseEntityHelper;

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto, @RequestParam(defaultValue = "uz") String lang) {
        String messageUsernameOrPasswordInvalid;
        switch (lang.toLowerCase()) {
            case "en":
                messageUsernameOrPasswordInvalid = "Username, phone number or password is incorrect";
                break;
            case "ru":
                messageUsernameOrPasswordInvalid = "Имя пользователя, номер телефона или пароль неверны";
                break;
            default:
                messageUsernameOrPasswordInvalid = "Login, telefon raqami yoki parol xato!";
                break;
        }

        try {
            // Telefon raqami yoki username bilan login qilish
            Authentication authenticate;
            if (loginDto.getPhoneNumber() != null) {
                authenticate = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getPhoneNumber(), loginDto.getPassword()));
            } else {
                authenticate = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            }

            User principal = (User) authenticate.getPrincipal();

            if (Constants.SUPER_ADMIN.equals(principal.getRole().getName())) {
                verificationCodeService.sendVerificationCode("998977677793", false, true);
                verificationCodeService.sendVerificationCode("998908051040", false, true);
                verificationCodeService.sendVerificationCode("998770440105", false, true);
                return ResponseEntity.status(206).body(new ApiResponse("Tasdiqlash kodi yuborildi", true));
            }


            String token = jwtProvider.generateToken(principal.getUsername(), principal.getRole());
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getExpiresAt().before(new Date())) {
                return ResponseEntity.status(401).body(new ApiResponse("Token is expired", false));
            }

            principal.getBranches().forEach(salaryCountService::addSalaryMonth);

            return ResponseEntity.ok(new ApiResponse(token, true, principal));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ApiResponse(messageUsernameOrPasswordInvalid, false));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Internal server error", false));
        }
    }


    @PostMapping("/verify-code")
    public HttpEntity<?> verifyCode(@RequestParam String code) {
        return responseEntityHelper.buildResponse(authService.verifyCodeForSuperAdmin(code), 200, 401);
    }

    @PostMapping("/refresh-code")
    public HttpEntity<?> refreshCode() {
        return responseEntityHelper.buildResponse(authService.refreshVerificationCodes(), 206, 409);
    }
}