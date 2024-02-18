package uz.pdp.springsecurity.hr.service.business;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;

public interface BusinessService {
    HttpEntity<Result> getBusinessRole(User user);
}
