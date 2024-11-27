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
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.LoginDto;
import uz.pdp.springsecurity.security.JwtProvider;
import uz.pdp.springsecurity.service.SalaryCountService;

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

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto, @RequestParam(defaultValue = "uz") String lang) {
        String messageUsernameOrPasswordInvalid;
        if (lang.equalsIgnoreCase("en")) {
            messageUsernameOrPasswordInvalid = "Username or password is incorrect";
        } else if (lang.equalsIgnoreCase("ru")) {
            messageUsernameOrPasswordInvalid = "Имя пользователя или пароль неверны";
        } else {
            messageUsernameOrPasswordInvalid = "Login yoki parol xato!";
        }
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            User principal = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(principal.getUsername(), principal.getRole());
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getExpiresAt().before(new Date())) {
                return ResponseEntity.status(401).body(new ApiResponse("Token is expired", false));
            }
            for (Branch branch : principal.getBranches()) {
                salaryCountService.addSalaryMonth(branch);
            }
            return ResponseEntity.status(200).body(new ApiResponse(token, true, principal));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ApiResponse(messageUsernameOrPasswordInvalid, false));
        }
    }
}