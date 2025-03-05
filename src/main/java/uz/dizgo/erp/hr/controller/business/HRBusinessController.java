package uz.dizgo.erp.hr.controller.business;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.service.business.BusinessService;

@RestController
@RequestMapping("/api/v1/hr/business")
@RequiredArgsConstructor
public class HRBusinessController {
    private final BusinessService service;
    @CheckPermission("VIEW_DASHBOARD")
    @GetMapping("/roles")
    public HttpEntity<Result> getBusinessRole(@CurrentUser User user){
        return service.getBusinessRole(user);
    }
}
