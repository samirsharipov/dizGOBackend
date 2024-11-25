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
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.service.AgreementService;
import uz.pdp.springsecurity.service.BusinessService;
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
    private final LidFieldRepository lidFieldRepository;
    private final SourceRepository sourceRepository;
    private final InvoiceService invoiceService;
    private final BalanceRepository balanceRepository;
    private final LanguageRepository languageRepository;

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

            Tariff tariff = tariffRepository.save(new Tariff(
                    "Premium", "Premium tariff", RolePermissions.SUPER_ADMIN_PERMISSIONS, 10, 0,
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


            //Lid field lar qo'shish
            List<LidField> lidFields = Arrays.asList(
                    new LidField("FIO", false, ValueType.STRING, business),
                    new LidField("PhoneNumber", false, ValueType.INTEGER, business)
            );
            lidFieldRepository.saveAll(lidFields);

            // Source qo'shish
            List<Source> sources = Arrays.asList(
                    new Source("Telegram", business),
                    new Source("Facebook", business),
                    new Source("Instagram", business),
                    new Source("HandleWrite", business)
            );
            sourceRepository.saveAll(sources);

            //Lid Status qo'shish
            List<LidStatus> lidStatuses = Arrays.asList(
                    new LidStatus("New", "rang", 1, "New", true, business),
                    new LidStatus("Progress", "rang", 2, "Progress", true, business),
                    new LidStatus("Rejection", "rang", 3, "Rejection", true, business),
                    new LidStatus("Done", "rang", 4, "Done", true, true, business)
            );
            lidStatusRepository.saveAll(lidStatuses);


            List<Business> all = businessRepository.findAll();
            for (Business business2 : all) {
                List<Shablon> all1 = shablonRepository.findAllByBusiness_Id(business2.getId());
                if (all1.isEmpty()) {
                    List<Shablon> shablons = Arrays.asList(
                            new Shablon("Tug'ilgan kun uchun", "bithday", "Hurmatli {ism} tugilgan kuningiz bilan", business2),
                            new Shablon("Mijozlar qarzi", "debtCustomer", "qarzingizni tulash vaqti keldi", business2),
                            new Shablon("Task qo'shilganda", "newTask", "yangi task qoshildi", business2)
                    );
                    shablonRepository.saveAll(shablons);
                }
            }

            brandRepository.save(new Brand(
                    "brand", business));

            subscriptionRepository.save(new Subscription(business, tariff, startDay, endDay,
                    StatusTariff.CONFIRMED, PayType.OFFLINE, true, true, false));

            Address address = addressRepository.save(new Address(
                    "Tashkent", "Shayxontuxur", "Gulobod", "1"));

            Role superAdmin = roleRepository.save(new Role(
                    Constants.SUPERADMIN,
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

            List<PaymentMethod> paymentMethods = Arrays.asList(
                    new PaymentMethod("Naqd", business),
                    new PaymentMethod("PlastikKarta", business),
                    new PaymentMethod("BankOrqali", business)
            );
            payMethodRepository.saveAll(paymentMethods);

            currencyRepository.save(new Currency(business, 11400));

            Branch mainBranch = branchRepository.save(new Branch("Bnav Main Branch ", address, business));
            branches.add(mainBranch);

            invoiceService.create(mainBranch);
            BusinessService.createProjectStatus(mainBranch, projectStatusRepository);

            List<TaskStatus> taskStatuses = Arrays.asList(
                    new TaskStatus("Completed", "Completed", 2, true, "#04d227", mainBranch),
                    new TaskStatus("Uncompleted", "Uncompleted", 1, true, "#FF0000", mainBranch)
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
                user.setArrivalTime("08:00");
                user.setLeaveTime("16:00");
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
                    new ExchangeStatus(KUTILMOQDA.name()),
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
        Optional<Role> superAdmin = roleRepository.findByName(Constants.SUPERADMIN);
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