package uz.dizgo.erp.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.LoginDto;
import uz.dizgo.erp.security.JwtProvider;
import uz.dizgo.erp.service.AuthService;
import uz.dizgo.erp.service.MessageService;
import uz.dizgo.erp.service.SalaryCountService;
import uz.dizgo.erp.utils.Constants;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final SalaryCountService salaryCountService;
    private final AuthService authService;
    private final ResponseEntityHelper responseEntityHelper;
    private final MessageService messageService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {
        try {
            log.info("Login request: username={}", loginDto.getUsername());

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            User principal = (User) authenticate.getPrincipal();
            log.info("User authenticated successfully: {}", principal.getUsername());

            if (Constants.SUPER_ADMIN.equals(principal.getRole().getName())) {
                authService.refreshVerificationCodes();
                return ResponseEntity.status(206)
                        .body(new ApiResponse(messageService.getMessage("verification.codes.sent"), true));
            }

            String token = jwtProvider.generateToken(principal.getUsername(), principal.getRole());
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getExpiresAt().before(new Date())) {
                return ResponseEntity.status(401).body(new ApiResponse(messageService.getMessage("token.is.expired"), false));
            }

            principal.getBranches().forEach(salaryCountService::addSalaryMonth);
            log.info("Login successful, token generated for user: {}", principal.getUsername());

            return ResponseEntity.ok(new ApiResponse(token, true, principal));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for username={} Reason: {}", loginDto.getUsername(), e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse(messageService.getMessage("username.password.incorrect"), false));
        } catch (Exception e) {
            log.error("Internal server error during login process for username={}", loginDto.getUsername(), e);
            return ResponseEntity.status(500).body(new ApiResponse(messageService.getMessage("error.message"), false));
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