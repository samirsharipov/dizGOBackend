package uz.dizgo.erp.hr.controller.employees;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.annotations.CheckPermission;
import uz.dizgo.erp.annotations.CurrentUser;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.hr.service.employess.EmployeesService;

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
