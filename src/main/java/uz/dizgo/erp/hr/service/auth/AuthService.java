package uz.dizgo.erp.hr.service.auth;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.payload.LoginDto;

public interface AuthService {
    HttpEntity<Result> loginHrUser(LoginDto loginDto);

    HttpEntity<Result> getMe(User user);
}
