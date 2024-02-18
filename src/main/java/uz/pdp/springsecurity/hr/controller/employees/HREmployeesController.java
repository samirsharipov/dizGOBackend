package uz.pdp.springsecurity.hr.controller.employees;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.annotations.CheckPermission;
import uz.pdp.springsecurity.annotations.CurrentUser;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;
import uz.pdp.springsecurity.hr.service.employess.EmployeesService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/employees")
@RequiredArgsConstructor
public class HREmployeesController {
    private final EmployeesService service;

    @CheckPermission("VIEW_DASHBOARD")
    @GetMapping("/list")
    public HttpEntity<Result> getAllBusinessEmployees(@CurrentUser User user, @RequestParam(defaultValue = "ALL") String role) {
        return service.getAllBusinessEmployees(user,role);
    }
    @GetMapping("/{branchId}")
    public HttpEntity<Result> getAllEmployeesByBranchId(@PathVariable UUID branchId) {
        return service.getAllEmployeesByBranchId(branchId);
    }
}
