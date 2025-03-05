package uz.dizgo.erp.hr.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.service.auth.AuthService;
import uz.dizgo.erp.payload.LoginDto;

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
