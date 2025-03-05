package uz.dizgo.erp.hr.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.service.product.ProductService;

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
