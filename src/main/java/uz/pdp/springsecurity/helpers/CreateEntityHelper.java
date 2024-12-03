package uz.pdp.springsecurity.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.StatusTariff;
import uz.pdp.springsecurity.mapper.AddressMapper;
import uz.pdp.springsecurity.mapper.BranchMapper;
import uz.pdp.springsecurity.payload.BranchDto;
import uz.pdp.springsecurity.payload.BusinessDto;
import uz.pdp.springsecurity.payload.UserDto;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.service.BranchService;
import uz.pdp.springsecurity.service.UserService;
import uz.pdp.springsecurity.utils.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateEntityHelper {
    private final TariffRepository tariffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
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
        UserDto userDto = businessDto.getUserDto();
        userDto.setBranchId(Set.of(branch.getId()));
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

    public  void createShablon(Business business, ShablonRepository shablonRepository) {
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
}
