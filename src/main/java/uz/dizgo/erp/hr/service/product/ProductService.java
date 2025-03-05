package uz.dizgo.erp.hr.service.product;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;

import java.util.UUID;

public interface ProductService {
    HttpEntity<Result> getAllProductsByFilter(UUID branchId, Integer page, Integer limit, UUID rastaId, User user);
}
