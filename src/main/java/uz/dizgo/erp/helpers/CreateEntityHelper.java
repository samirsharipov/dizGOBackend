package uz.dizgo.erp.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.*;
import uz.dizgo.erp.repository.*;
import uz.dizgo.erp.entity.template.RolePermissions;
import uz.dizgo.erp.enums.StatusTariff;
import uz.dizgo.erp.mapper.BranchMapper;
import uz.dizgo.erp.payload.BranchDto;
import uz.dizgo.erp.payload.BusinessDto;
import uz.dizgo.erp.payload.UserCreateDto;
import uz.dizgo.erp.payload.UserDTO;
import uz.dizgo.erp.service.BranchService;
import uz.dizgo.erp.service.UserService;
import uz.dizgo.erp.utils.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateEntityHelper {
    private final TariffRepository tariffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final BalanceRepository balanceRepository;
    private final PayMethodRepository payMethodRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public void createSubscription(Business business, UUID tariffId) {
        Subscription subscription = new Subscription();
        subscription.setBusiness(business);
        tariffRepository.findById(tariffId).ifPresent(subscription::setTariff);
        subscription.setActive(false);
        subscription.setStatusTariff(StatusTariff.WAITING);
        subscriptionRepository.save(subscription);
    }


    public Branch createBranch(Business business, Address address, BranchDto branchDto) {
        branchDto.setAddressId(address.getId());
        branchDto.setBusinessId(business.getId());
        return branchRepository.save(branchMapper.toEntity(branchDto));
    }

    public void createBalance(Branch branch) {
        BranchService.createBalance(branch, balanceRepository, payMethodRepository);
    }

    public void createAdminRoleAndUser(Business business, Branch branch, BusinessDto businessDto) {
        // Admin rolini yaratish
        Role adminRole = new Role();
        adminRole.setName(Constants.ADMIN);
        adminRole.setPermissions(businessDto.getPermissionsList());
        adminRole.setBusiness(business);
        Role savedRole = roleRepository.save(adminRole);

        // Foydalanuvchi ma'lumotlarini sozlash
        UserCreateDto userCreateDto = businessDto.getUserDto();
        UserDTO userDto = new UserDTO();

        userDto.setUsername(userCreateDto.getUsername());
        userDto.setPassword(userCreateDto.getPassword());
        userDto.setFirstName(userCreateDto.getFirstName());
        userDto.setLastName(userCreateDto.getLastName());
        userDto.setPhoneNumber(businessDto.getBusinessNumber());
        userDto.setEnabled(false);

        userDto.setBranchIds(Set.of(branch.getId()));
        userDto.setRoleId(savedRole.getId());
        userDto.setBusinessId(business.getId());

        // Foydalanuvchini yaratish
        userService.add(userDto, true);
    }


    public void createProjectStatus(Branch branch, ProjectStatusRepository projectStatusRepository) {
        List<ProjectStatus> projectStatusList = Arrays.asList(
                new ProjectStatus("Process", "yellow", branch),
                new ProjectStatus("Uncompleted", "red", branch),
                new ProjectStatus("Completed", "green", branch)
        );
        projectStatusRepository.saveAll(projectStatusList);
    }

    public void createShablon(Business business, ShablonRepository shablonRepository) {
        List<Shablon> shablonList = Arrays.asList(
                new Shablon("Tug'ilgan kun uchun", "bithday", "Hurmatli {ism} tugilgan kuningiz bilan", business),
                new Shablon("Mijozlar qarzi", "debtCustomer", "qarzingizni tulash vaqti keldi", business),
                new Shablon("Task qo'shilganda", "newTask", "yangi task qoshildi", business)
        );
        shablonRepository.saveAll(shablonList);
    }

    public static void saveLidStatus(Business business, LidStatusRepository lidStatusRepository) {
        List<LidStatus> lidStatuses = Arrays.asList(
                new LidStatus("New", "rang", 1, "New", true, business),
                new LidStatus("Progress", "rang", 2, "Progress", true, business),
                new LidStatus("Rejection", "rang", 3, "Rejection", true, business),
                new LidStatus("Done", "rang", 4, "Done", true, true, business)
        );
        lidStatusRepository.saveAll(lidStatuses);
    }

    public  void saveRole(Business business, RoleRepository roleRepository) {

        roleRepository.save(new Role(
                Constants.MANAGER,
                RolePermissions.MANAGER_PERMISSIONS_FOR_OTHERS,
                business));

        roleRepository.save(new Role(
                Constants.CASHIER,
                RolePermissions.CASHIER_PERMISSIONS,
                business));
    }
}
