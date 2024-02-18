package uz.pdp.springsecurity.hr.controller.business;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.service.business.BusinessService;

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
