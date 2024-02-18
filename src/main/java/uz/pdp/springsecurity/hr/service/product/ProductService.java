package uz.pdp.springsecurity.hr.service.product;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;

import java.util.UUID;

public interface ProductService {
    HttpEntity<Result> getAllProductsByFilter(UUID branchId, Integer page, Integer limit, UUID rastaId, User user);
}
