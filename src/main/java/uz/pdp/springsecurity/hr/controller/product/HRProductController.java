package uz.pdp.springsecurity.hr.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.service.product.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/product")
@RequiredArgsConstructor
public class HRProductController {
    private final ProductService service;

    @GetMapping
    public HttpEntity<Result> getAllProductsByFilter(@RequestParam(required = false) UUID branchId,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "15") Integer limit,
                                                     @RequestParam(required = false) UUID rastaId,
                                                     @CurrentUser User user) {
        return service.getAllProductsByFilter(branchId, page, limit, rastaId, user);
    }
}
