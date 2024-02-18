package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.Salary;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.mapper.SalaryMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.SalaryDto;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;
    private final SalaryCountRepository salaryCountRepository;
    private final WorkTimeRepository workTimeRepository;
    private final SalaryMapper salaryMapper;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final WorkTimeLateService workTimeLateService;

    @Autowired
    @Lazy
    private PrizeService prizeService;

    private final BalanceService balanceService;


    public void add(User user, Branch branch, double salarySum) {
        Optional<Salary> optionalSalary = salaryRepository.findByUserIdAndBranch_IdAndActiveTrue(user.getId(), branch.getId());
        if (optionalSalary.isEmpty()) {
            Date date = new Date();
            salaryRepository.save(new Salary(
                    user,
                    branch,
                    0d,
                    salarySum,
                    date,
                    date
            ));
            return;
        }
        Salary salary = optionalSalary.get();
        salary.setSalary(salary.getSalary() + salarySum);
        salaryRepository.save(salary);
    }


    @Transactional
    public ApiResponse paySalary(UUID salaryId, SalaryDto salaryDto) {
        Optional<Salary> optionalSalary = salaryRepository.findByIdAndActiveTrue(salaryId);
        if (optionalSalary.isEmpty()) return new ApiResponse("SALARY NOT FOUND", false);
        Salary salary = optionalSalary.get();
        double totalSalary = salary.getRemain() + salary.getSalary();

        User user = salary.getUser();
        Branch branch = salary.getBranch();
        Date now = new Date();
        salary.setPayedSum(salary.getPayedSum() + salaryDto.getSalary());
        salary.setDescription(salaryDto.getDescription());
        salary.setEndDate(now);
        salary.setActive(false);
        Salary newSalary = new Salary(
                user,
                branch,
                totalSalary - salary.getPayedSum(),
                now,
                now
        );
        try {
            salaryCountRepository.deleteAllByAgreement_UserIdAndBranchId(user.getId(), branch.getId());
            workTimeRepository.deleteAllByUserIdAndBranchIdAndActiveFalse(user.getId(), branch.getId());
            prizeService.deActive(user.getId(), branch.getId());
            workTimeLateService.clear(user, branch);
            salaryRepository.save(salary);
            salaryRepository.save(newSalary);

            balanceService.edit(salary.getBranch().getId(), salaryDto.getSalary(), false, salaryDto.getPaymentMethodId());

            return new ApiResponse("SUCCESS", true);
        } catch (Exception e) {
            return new ApiResponse("ERROR", false);
        }
    }

    public ApiResponse payAvans(UUID salaryId, SalaryDto salaryDto) {
        double payedSum = salaryDto.getSalary();
        Optional<Salary> optionalSalary = salaryRepository.findByIdAndActiveTrue(salaryId);
        if (optionalSalary.isEmpty()) return new ApiResponse("NOT FOUND SALARY", false);
        Salary salary = optionalSalary.get();
        salary.setPayedSum(salary.getPayedSum() + payedSum);
        salary.setDescription(salaryDto.getDescription());
        salary.setEndDate(new Date());
        salaryRepository.save(salary);

        balanceService.edit(salary.getBranch().getId(), salaryDto.getSalary(), false, salaryDto.getPaymentMethodId());

        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getAll(UUID branchId) {
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) return new ApiResponse("NOT FOUND BRANCH", false);
        checkBeforeSalary(optionalBranch.get());
        List<Salary> salaryList = salaryRepository.findAllByBranchIdAndActiveTrue(branchId);
        if (salaryList.isEmpty()) return new ApiResponse("NOT FOUND SALARY");
        return new ApiResponse(true, salaryMapper.toDtoList(salaryList));
    }

    private void checkBeforeSalary(Branch branch) {
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
        if (optionalRole.isEmpty()) return;
        List<User> userList = userRepository.findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(branch.getId(), optionalRole.get());
        if (userList.isEmpty()) return;
        Date date = new Date();
        for (User user : userList) {
            if (!salaryRepository.existsByUserIdAndBranch_IdAndActiveTrue(user.getId(), branch.getId())) {
                salaryRepository.save(new Salary(
                        user,
                        branch,
                        date,
                        date
                ));
            }
        }
    }

    public ApiResponse getOne(UUID salaryId) {
        Optional<Salary> optionalSalary = salaryRepository.findByIdAndActiveTrue(salaryId);
        return optionalSalary.map(salary -> new ApiResponse(true, salaryMapper.toDto(salary))).orElseGet(() -> new ApiResponse("SALARY NOT FOUND", false));
    }

    public ApiResponse getAllByUser(UUID userId, UUID branchId) {
        if (!userRepository.existsById(userId)) return new ApiResponse("NOT FOUND USER", false);
        if (!branchRepository.existsById(branchId)) return new ApiResponse("NOT FOUND BRANCH", false);
        List<Salary> salaryList = salaryRepository.findAllByUserIdAndBranchId(userId, branchId);
        if (salaryList.isEmpty()) return new ApiResponse("NOT FOUND SALARY");
        return new ApiResponse(true, salaryMapper.toDtoList(salaryList));
    }

    public ApiResponse getByUserLast(UUID userId, UUID branchId) {
        if (!userRepository.existsById(userId)) return new ApiResponse("NOT FOUND USER", false);
        if (!branchRepository.existsById(branchId)) return new ApiResponse("NOT FOUND BRANCH", false);
        Optional<Salary> optionalSalary = salaryRepository.findByUserIdAndBranch_IdAndActiveTrue(userId, branchId);
        return optionalSalary.map(salary -> new ApiResponse(true, salaryMapper.toDto(salary))).orElseGet(() -> new ApiResponse("SALARY FOUND BRANCH", false));
    }
}
