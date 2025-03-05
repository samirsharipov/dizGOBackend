package uz.dizgo.erp.hr.service.business;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;

public interface BusinessService {
    HttpEntity<Result> getBusinessRole(User user);
}
