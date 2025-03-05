package uz.dizgo.erp.hr.service.employess;

import org.springframework.http.HttpEntity;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.payload.Result;

import java.util.UUID;

public interface EmployeesService {
    HttpEntity<Result> getAllBusinessEmployees(User user, String role);

    HttpEntity<Result> getAllEmployeesByBranchId(UUID branchId);
}
