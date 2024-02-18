package uz.pdp.springsecurity.hr.service.auth;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.payload.LoginDto;

public interface AuthService {
    HttpEntity<Result> loginHrUser(LoginDto loginDto);

    HttpEntity<Result> getMe(User user);
}
