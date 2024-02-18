package uz.pdp.springsecurity.hr.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.service.auth.AuthService;
import uz.pdp.springsecurity.payload.LoginDto;

@RestController
@RequestMapping("/api/v1/hr/auth")
@RequiredArgsConstructor
public class HRAuthController {
    private final AuthService service;

    @PostMapping("/login")
    public HttpEntity<Result> loginHrUser(@RequestBody LoginDto loginDto) {
        return service.loginHrUser(loginDto);
    }

    @GetMapping("/me")
    public HttpEntity<Result> getMe(@CurrentUser User user) {
        return service.getMe(user);
    }
}
