package uz.pdp.springsecurity.hr.service.employess;

import org.springframework.http.HttpEntity;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.hr.payload.Result;

import java.util.UUID;

public interface EmployeesService {
    HttpEntity<Result> getAllBusinessEmployees(User user, String role);

    HttpEntity<Result> getAllEmployeesByBranchId(UUID branchId);
}
