package uz.dizgo.erp.hr.service.employess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.hr.exception.HRException;
import uz.dizgo.erp.hr.payload.BonusPayload;
import uz.dizgo.erp.hr.payload.EmployeesPageTableResponse;
import uz.dizgo.erp.hr.payload.Result;
import uz.dizgo.erp.repository.BranchRepository;
import uz.dizgo.erp.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeesServiceImpl implements EmployeesService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Override
    public HttpEntity<Result> getAllBusinessEmployees(User user, String role) {
        if (role.equalsIgnoreCase("ALL")) {
            List<EmployeesPageTableResponse> responses = userRepository
                    .findAllByBusiness_Id(user.getBusiness().getId())
                    .stream()
                    .map(employee -> {
                        List<BonusPayload> bonusPayloads = employee.getBonuses()
                                .stream()
                                .map(bonus -> new BonusPayload(
                                        bonus.getName(),
                                        bonus.getColor(),
                                        bonus.getIcon(),
                                        bonus.getSumma(),
                                        bonus.isActive(),
                                        bonus.isDelete()
                                ))
                                .collect(Collectors.toList());

                        String jobName = (employee.getJob() != null) ? employee.getJob().getName() : "Unknown";
                        String phoneNumber = (employee.getPhoneNumber() != null) ? employee.getPhoneNumber() : "Unknown";
                        return new EmployeesPageTableResponse(
                                employee.getId(),
                                employee.getFirstName(),
                                employee.getLastName(),
                                phoneNumber,
                                jobName,
                                employee.getRole().getName(),
                                bonusPayloads
                        );
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new Result(true, "Xodimlar ro'yxati", responses));
        } else {
            List<EmployeesPageTableResponse> responses = userRepository
                    .findAllByBusiness_IdAndRoleId(user.getBusiness().getId(), UUID.fromString(role))
                    .stream()
                    .map(employee -> {
                        List<BonusPayload> bonusPayloads = employee.getBonuses()
                                .stream()
                                .map(bonus -> new BonusPayload(
                                        bonus.getName(),
                                        bonus.getColor(),
                                        bonus.getIcon(),
                                        bonus.getSumma(),
                                        bonus.isActive(),
                                        bonus.isDelete()
                                ))
                                .collect(Collectors.toList());

                        String jobName = (employee.getJob() != null) ? employee.getJob().getName() : "Unknown";
                        String phoneNumber = (employee.getPhoneNumber() != null) ? employee.getPhoneNumber() : "Unknown";
                        return new EmployeesPageTableResponse(
                                employee.getId(),
                                employee.getFirstName(),
                                employee.getLastName(),
                                phoneNumber,
                                jobName,
                                employee.getRole().getName(),
                                bonusPayloads
                        );
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new Result(true, "Xodimlar ro'yxati", responses));

        }
    }

    @Override
    public HttpEntity<Result> getAllEmployeesByBranchId(UUID branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new HRException("Filial topilmadi!"));
        List<User> users = userRepository.findAllByBranches_Id(branch.getId());
        List<EmployeesPageTableResponse> responses = new LinkedList<>();
        for (User user : users) {
            String jobName = (user.getJob() != null) ? user.getJob().getName() : "Unknown";
            String phoneNumber = (user.getPhoneNumber() != null) ? user.getPhoneNumber() : "Unknown";
            responses.add(new EmployeesPageTableResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    phoneNumber,
                    jobName,
                    user.getRole().getName()
            ));
        }
        return ResponseEntity.ok(new Result(true, "Xodimlar ro'yxati", responses));
    }


}
