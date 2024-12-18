package uz.pdp.springsecurity.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.entity.template.RolePermissions;
import uz.pdp.springsecurity.enums.*;
import uz.pdp.springsecurity.helpers.BusinessHelper;
import uz.pdp.springsecurity.helpers.CreateEntityHelper;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.service.AgreementService;
import uz.pdp.springsecurity.service.InvoiceService;
import uz.pdp.springsecurity.utils.Constants;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static uz.pdp.springsecurity.enums.ExchangeStatusName.*;
import static uz.pdp.springsecurity.enums.StatusName.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProjectStatusRepository projectStatusRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PayMethodRepository payMethodRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeStatusRepository exchangeStatusRepository;
    private final BusinessRepository businessRepository;
    private final BranchRepository branchRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final AddressRepository addressRepository;
    private final BrandRepository brandRepository;
    private final TariffRepository tariffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AgreementService agreementService;
    private final LidStatusRepository lidStatusRepository;
    private final InvoiceService invoiceService;
    private final BalanceRepository balanceRepository;
    private final LanguageRepository languageRepository;
    private final CreateEntityHelper createEntityHelper;
    private final BusinessHelper businessHelper;
    private final BranchCategoryRepository branchCategoryRepository;

    @Value("${spring.sql.init.mode}")
    private String initMode;
    private final ShablonRepository shablonRepository;

    @Override
    public void run(String... args) {
        if (initMode.equals("always")) {

            Timestamp startDay = new Timestamp(System.currentTimeMillis());
            LocalDate date = LocalDate.now().plusMonths(1);
            Timestamp endDay = Timestamp.valueOf(date.atStartOfDay());
            Set<Branch> branches = new HashSet<>();

            Tariff tariffSuperAdmin = tariffRepository.save(new Tariff(
                    "Super Admin", "Super admin tariff", RolePermissions.SUPER_ADMIN_PERMISSIONS, 10, 0,
                    0, 0, Lifetime.MONTH, 0, 1, 100, 0, false, true));

            tariffRepository.save(new Tariff(
                    "Freemium", "Freemium tariff", RolePermissions.ADMIN_PERMISSIONS, 10, 0,
                    0, 0, Lifetime.MONTH, 0, 1, 100, 0, true, false));

            tariffRepository.save(new Tariff(
                    "Premium", "Premium tariff", RolePermissions.ADMIN_PERMISSIONS, 10, 0,
                    0, 0, Lifetime.MONTH, 0, 1, 100, 0, true, false));

            Business business = businessRepository.save(new Business(
                    "Business Navigator", "BNAV", true, false, true));


            // Language qo'shish
            List<Language> languages = Arrays.asList(
                    new Language("uz", "Uzbek language", "Uzbek"),
                    new Language("ru", "Russian language", "Russian"),
                    new Language("en", "English language", "English")
            );
            languageRepository.saveAll(languages);


            // Qo'shimcha ma'lumotlar yaratish
            businessHelper.createStatusAndOther(business);

            //Lid Status qo'shish
            CreateEntityHelper.saveLidStatus(business, lidStatusRepository);


            List<Business> all = businessRepository.findAll();
            for (Business business2 : all) {
                List<Shablon> all1 = shablonRepository.findAllByBusiness_Id(business2.getId());
                if (all1.isEmpty()) {
                    createEntityHelper.createShablon(business, shablonRepository);
                }
            }

            brandRepository.save(new Brand(
                    "brand", business));

            subscriptionRepository.save(new Subscription(business, tariffSuperAdmin, startDay, endDay,
                    StatusTariff.CONFIRMED, PayType.OFFLINE, true, true, false));

//            Address address = addressRepository.save(new Address(
//                    "Tashkent", "Shayxontuxur", "Gulobod", "1"));

            Role superAdmin = roleRepository.save(new Role(
                    Constants.SUPER_ADMIN,
                    RolePermissions.SUPER_ADMIN_PERMISSIONS,
                    business));

            Role admin = roleRepository.save(new Role(
                    Constants.ADMIN,
                    RolePermissions.ADMIN_PERMISSIONS,
                    business));

            Role manager = roleRepository.save(new Role(
                    Constants.MANAGER,
                    RolePermissions.MANAGER_PERMISSIONS,
                    business));

            Role employee = roleRepository.save(new Role(
                    Constants.EMPLOYEE,
                    RolePermissions.EMPLOYEE_PERMISSIONS,
                    business));

            roleRepository.save(new Role(
                    Constants.CUSTOMER,
                    RolePermissions.CUSTOMER_PERMISSIONS,
                    business
            ));

            List<PaymentMethod> paymentMethods = Arrays.asList(
                    new PaymentMethod("Naqd", false, true),
                    new PaymentMethod("UzCard", true, false),
                    new PaymentMethod("Humo", true, false),
                    new PaymentMethod("BankOrqali", false, false)
            );
            payMethodRepository.saveAll(paymentMethods);

            currencyRepository.save(new Currency(business, 11400));

            Address address = new Address();
            address.setName("O'zbekiston");
            addressRepository.save(address);
            Branch main_branch_ = new Branch("Bnav Main Branch ", address, business);
            main_branch_.setBranchCategory(branchCategoryRepository.save(new BranchCategory("Oziq ovqat", "asosiy")));
            branchRepository.save(main_branch_);
            branches.add(main_branch_);

            invoiceService.create(main_branch_);
            createEntityHelper.createProjectStatus(main_branch_, projectStatusRepository);

            List<TaskStatus> taskStatuses = Arrays.asList(
                    new TaskStatus("Completed", "Completed", 2, true, "#04d227", main_branch_),
                    new TaskStatus("Uncompleted", "Uncompleted", 1, true, "#FF0000", main_branch_)
            );
            taskStatusRepository.saveAll(taskStatuses);

            // Userlarni qo'shish
            List<User> users = Arrays.asList(
                    new User("Admin", "Admin", "admin", passwordEncoder.encode("123"),
                            admin, true, business, branches, true),

                    new User("SuperAdmin", "Admin of site", "superadmin", passwordEncoder.encode("123"),
                            superAdmin, true, business, branches, true),

                    new User("Manager", "manager", "manager", passwordEncoder.encode("123"),
                            manager, true, business, branches, true),

                    new User("Employee", "employee", "employee", passwordEncoder.encode("123"),
                            employee, true, business, branches, true)
            );

            //vaqtlarni o'rnatish
            users.forEach(user -> {
                user.setArrivalTime("09:00");
                user.setLeaveTime("18:00");
                user.setPassportNumber("123");
                userRepository.save(user);
                agreementService.add(user);
            });

            //PaymentStatus ob'ektini qo'shish
            List<PaymentStatus> paymentStatuses = Arrays.asList(
                    new PaymentStatus(TOLANGAN.name()),
                    new PaymentStatus(QISMAN_TOLANGAN.name()),
                    new PaymentStatus(TOLANMAGAN.name())
            );
            paymentStatusRepository.saveAll(paymentStatuses);


            List<Balance> balancesToSave = new ArrayList<>();
            List<String> currencies = Arrays.asList("SOM", "DOLLAR");

            for (String currency : currencies) {
                for (PaymentMethod paymentMethod : paymentMethods) {
                    for (Branch branch : branches) {
                        Balance balance = new Balance();
                        balance.setAccountSumma(0);
                        balance.setPaymentMethod(paymentMethod);
                        balance.setBranch(branch);
                        balance.setCurrency(currency);

                        balancesToSave.add(balance);
                    }
                }
            }
            balanceRepository.saveAll(balancesToSave);

            List<ExchangeStatus> exchangeStatuses = Arrays.asList(
                    new ExchangeStatus(BUYURTMA_QILINGAN.name()),
                    new ExchangeStatus(QABUL_QILINGAN.name())
            );
            exchangeStatusRepository.saveAll(exchangeStatuses);

        } else if (initMode.equals("never")) {
//            updatePermission(); // TODO: 5/29/2023 if you add new permission
        }
    }

    private void updatePermission() {
        List<Permissions> newPermissionList = Arrays.asList(
                // TODO: 5/29/2023 write new permissions here
        );
        Optional<Role> superAdmin = roleRepository.findByName(Constants.SUPER_ADMIN);
        List<Role> adminList = roleRepository.findAllByName(Constants.ADMIN);
        superAdmin.ifPresent(adminList::add);
        updatePermissionHelperRole(adminList, newPermissionList);
        updatePermissionHelperTariff(tariffRepository.findAll(), newPermissionList);
    }

    private void updatePermissionHelperRole(List<Role> roleList, List<Permissions> newPermissionList) {
        for (Role role : roleList) {
            Set<Permissions> permissions = new HashSet<>(role.getPermissions());
            permissions.addAll(newPermissionList);
            role.setPermissions(new ArrayList<>(permissions));
        }
        roleRepository.saveAll(roleList);
    }

    private void updatePermissionHelperTariff(List<Tariff> tariffList, List<Permissions> newPermissionList) {
        for (Tariff tariff : tariffList) {
            Set<Permissions> permissions = new HashSet<>(tariff.getPermissions());
            permissions.addAll(newPermissionList);
            tariff.setPermissions(new ArrayList<>(permissions));
        }
        tariffRepository.saveAll(tariffList);
    }
}