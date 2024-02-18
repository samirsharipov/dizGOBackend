package uz.pdp.springsecurity.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.enums.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.service.AgreementService;
import uz.pdp.springsecurity.service.BusinessService;
import uz.pdp.springsecurity.service.InvoiceService;
import uz.pdp.springsecurity.util.Constants;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static uz.pdp.springsecurity.enums.ExchangeStatusName.*;
import static uz.pdp.springsecurity.enums.Permissions.*;
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
    private final CategoryRepository categoryRepository;
    private final TariffRepository tariffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MeasurementRepository measurementRepository;
    private final AgreementService agreementService;
    private final LidStatusRepository lidStatusRepository;
    private final LidFieldRepository lidFieldRepository;
    private final SourceRepository sourceRepository;
    private final InvoiceService invoiceService;
    private final BalanceRepository balanceRepository;

    @Value("${spring.sql.init.mode}")
    private String initMode;
    private final ShablonRepository shablonRepository;

    @Override
    public void run(String... args) {
        if (initMode.equals("always")) {
            Permissions[] permissions = Permissions.values();

            List<Tariff> tariffRepositoryAll = tariffRepository.findAll();
            Tariff tariff = null;
            if (tariffRepositoryAll.isEmpty()) {
                tariff = new Tariff(
                        "test tariff",
                        "test uchun",
                        List.of(permissions),
                        10,
                        0,
                        0,
                        0,
                        Lifetime.MONTH,
                        0,
                        1,
                        100,
                        0,
                        true,
                        false
                );
                tariffRepository.save(tariff);
            }

            Business business = new Business();
            business.setDescription("Test Uchun");
            business.setName("Application");
            business.setActive(true);
            business.setDelete(false);
            business = businessRepository.save(business);


            LidField lidField = new LidField();
            lidField.setName("FIO");
            lidField.setBusiness(business);
            lidField.setValueType(ValueType.STRING);
            lidField.setTanlangan(false);
            lidFieldRepository.save(lidField);

            LidField lidField1 = new LidField();
            lidField1.setName("PhoneNumber");
            lidField1.setBusiness(business);
            lidField1.setValueType(ValueType.INTEGER);
            lidField1.setTanlangan(false);
            lidFieldRepository.save(lidField1);

            Source source = new Source();
            source.setBusiness(business);
            source.setName("Telegram");
            sourceRepository.save(source);
            Source source1 = new Source();
            source1.setBusiness(business);
            source1.setName("Facebook");
            sourceRepository.save(source1);
            Source source2 = new Source();
            source2.setBusiness(business);
            source2.setName("Instagram");
            sourceRepository.save(source2);
            Source source3 = new Source();
            source3.setBusiness(business);
            source3.setName("HandleWrite");
            sourceRepository.save(source3);

            LidStatus newStatus = new LidStatus();
            newStatus.setName("New");
            newStatus.setIncrease(true);
            newStatus.setOrginalName("New");
            newStatus.setColor("rang");
            newStatus.setSort(1);
            newStatus.setBusiness(business);
            lidStatusRepository.save(newStatus);

            LidStatus progressStatus = new LidStatus();
            progressStatus.setName("Progress");
            progressStatus.setIncrease(true);
            progressStatus.setOrginalName("Progress");
            progressStatus.setColor("rang");
            progressStatus.setSort(2);
            progressStatus.setBusiness(business);
            lidStatusRepository.save(progressStatus);

            LidStatus rejectionStatus = new LidStatus();
            rejectionStatus.setName("Rejection");
            rejectionStatus.setIncrease(true);
            rejectionStatus.setOrginalName("Rejection");
            rejectionStatus.setColor("rang");
            rejectionStatus.setSort(3);
            rejectionStatus.setBusiness(business);
            lidStatusRepository.save(rejectionStatus);


            LidStatus doneStatus = new LidStatus();
            doneStatus.setName("Done");
            doneStatus.setIncrease(true);
            doneStatus.setOrginalName("Done");
            doneStatus.setColor("rang");
            doneStatus.setSaleStatus(true);
            doneStatus.setSort(4);
            doneStatus.setBusiness(business);
            lidStatusRepository.save(doneStatus);


            List<Business> businessRepositoryAll = businessRepository.findAll();
            for (Business business2 : businessRepositoryAll) {
                List<Shablon> all1 = shablonRepository.findAllByBusiness_Id(business2.getId());
                if (all1.isEmpty()) {
                    Shablon shablon = new Shablon();
                    shablon.setName("Tug'ilgan kun uchun");
                    shablon.setOriginalName("bithday");
                    shablon.setMessage("Hurmatli {ism} tugilgan kuningiz bilan");
                    shablon.setBusiness(business2);
                    shablonRepository.save(shablon);

                    Shablon shablon2 = new Shablon();
                    shablon2.setName("Mijozlar qarzi");
                    shablon2.setOriginalName("debtCustomer");
                    shablon2.setMessage("qarzingizni tulash vaqti keldi");
                    shablon2.setBusiness(business2);
                    shablonRepository.save(shablon2);

                    Shablon shablon3 = new Shablon();
                    shablon3.setName("Task qo'shilganda");
                    shablon3.setOriginalName("newTask");
                    shablon3.setMessage("yangi task qoshildi");
                    shablon3.setBusiness(business2);
                    shablonRepository.save(shablon3);
                }
            }

            measurementRepository.save(
                    new Measurement("dona",
                            business)
            );

            brandRepository.save(
                    new Brand(
                            "brand",
                            business
                    ));

            categoryRepository.save(
                    new Category(
                            "category",
                            business
                    ));

            Timestamp startDay = new Timestamp(System.currentTimeMillis());
            LocalDate date = LocalDate.now().plusMonths(1);
            Timestamp endDay = Timestamp.valueOf(date.atStartOfDay());

            subscriptionRepository.save(new Subscription(
                    business,
                    tariff,
                    startDay,
                    endDay,
                    StatusTariff.CONFIRMED,
                    PayType.OFFLINE,
                    true,
                    true,
                    false
            ));

            Address address = addressRepository.save(new Address(
                    "Tashkent",
                    "Shayxontuxur",
                    "Gulobod",
                    "1"
            ));

            assert tariff != null;
            Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveTrue(business.getId());
            Subscription subscription = new Subscription();
            if (optionalSubscription.isPresent()) {
                subscription = optionalSubscription.get();
            }
            Role superAdmin = roleRepository.save(new Role(
                    Constants.SUPERADMIN,
                    subscription.getTariff().getPermissions(),
                    business
            ));

            Role admin = roleRepository.save(
                    new Role(
                            Constants.ADMIN,
                            Arrays.asList(
                                    GET_TARIFF,

                                    ADD_ADDRESS,
                                    EDIT_ADDRESS,
                                    VIEW_ADDRESS,
                                    DELETE_ADDRESS,

                                    UPLOAD_MEDIA,
                                    DOWNLOAD_MEDIA,
                                    VIEW_MEDIA_INFO,
                                    DELETE_MEDIA,

                                    ADD_BRANCH,
                                    EDIT_BRANCH,
                                    VIEW_BRANCH_ADMIN,
                                    VIEW_BRANCH,
                                    DELETE_BRANCH,

                                    ADD_BRAND,
                                    EDIT_BRAND,
                                    VIEW_BRAND,
                                    DELETE_BRAND,

                                    ADD_CATEGORY,
                                    EDIT_CATEGORY,
                                    VIEW_CATEGORY,
                                    VIEW_CATEGORY_ADMIN,
                                    DELETE_CATEGORY,
                                    ADD_CHILD_CATEGORY,

                                    ADD_CURRENCY,
                                    EDIT_CURRENCY,
                                    VIEW_CURRENCY,
                                    DELETE_CURRENCY,

                                    ADD_CUSTOMER,
                                    EDIT_CUSTOMER,
                                    VIEW_CUSTOMER,
                                    VIEW_CUSTOMER_ADMIN,
                                    DELETE_CUSTOMER,

                                    ADD_MEASUREMENT,
                                    EDIT_MEASUREMENT,
                                    VIEW_MEASUREMENT,
                                    VIEW_MEASUREMENT_ADMIN,
                                    DELETE_MEASUREMENT,

                                    ADD_OUTLAY,
                                    EDIT_OUTLAY,
                                    VIEW_OUTLAY,
                                    VIEW_OUTLAY_ADMIN,
                                    DELETE_OUTLAY,

                                    ADD_PRODUCT,
                                    EDIT_PRODUCT,
                                    VIEW_PRODUCT,

                                    VIEW_PRODUCT_ADMIN,
                                    DELETE_PRODUCT,

                                    ADD_ROLE,
                                    EDIT_ROLE,
                                    VIEW_ROLE,
                                    VIEW_ROLE_ADMIN,
                                    DELETE_ROLE,

                                    ADD_SUPPLIER,
                                    EDIT_SUPPLIER,
                                    VIEW_SUPPLIER,
                                    VIEW_SUPPLIER_ADMIN,
                                    DELETE_SUPPLIER,

                                    ADD_USER,
                                    EDIT_USER,
                                    VIEW_USER,
                                    VIEW_USER_ADMIN,
                                    DELETE_USER,
                                    EDIT_MY_PROFILE,

                                    ADD_TRADE,
                                    EDIT_TRADE,
                                    VIEW_TRADE,
                                    VIEW_TRADE_ADMIN,
                                    DELETE_TRADE,
                                    DELETE_MY_TRADE,
                                    VIEW_MY_TRADE,

                                    ADD_PAY_METHOD,
                                    EDIT_PAY_METHOD,
                                    VIEW_PAY_METHOD,
                                    VIEW_PAY_METHOD_ADMIN,
                                    DELETE_PAY_METHOD,

                                    ADD_PAY_STATUS,
                                    EDIT_PAY_STATUS,
                                    VIEW_PAY_STATUS,
                                    VIEW_PAY_STATUS_ADMIN,
                                    DELETE_PAY_STATUS,

                                    ADD_PURCHASE,
                                    EDIT_PURCHASE,
                                    VIEW_PURCHASE,
                                    VIEW_PURCHASE_ADMIN,
                                    DELETE_PURCHASE,

                                    ADD_EXCHANGE,
                                    EDIT_EXCHANGE,
                                    VIEW_EXCHANGE,
                                    VIEW_EXCHANGE_ADMIN,
                                    DELETE_EXCHANGE,

                                    VIEW_BENEFIT_AND_LOST,

                                    ADD_CUSTOMER_GROUP,
                                    DELETE_CUSTOMER_GROUP,
                                    EDIT_CUSTOMER_GROUP,
                                    VIEW_CUSTOMER_GROUP,

                                    ADD_TAX,
                                    DELETE_TAX,
                                    EDIT_TAX,
                                    VIEW_TAX,
                                    ADD_PRODUCT_TYPE,
                                    GET_PRODUCT_TYPE,

                                    UPDATE_PRODUCT_TYPE,
                                    DELETE_PRODUCT_TYPE,

                                    GET_EXCEL,
                                    POST_EXCEL,

                                    VIEW_INFO,
                                    VIEW_INFO_ADMIN,


                                    CREATE_CONTENT,
                                    EDIT_CONTENT,
                                    GET_CONTENT,
                                    DELETE_CONTENT,

                                    CREATE_PRODUCTION,
                                    GET_PRODUCTION,
                                    VIEW_REPORT,

                                    GET_COURSE,


                                    ADD_PROJECT_TYPE,
                                    EDIT_PROJECT_TYPE,
                                    GET_PROJECT_TYPE,
                                    DELETE_PROJECT_TYPE,

                                    DELETE_TASK_STATUS,
                                    GET_TASK_STATUS,
                                    EDIT_TASK_STATUS,
                                    ADD_TASK_STATUS,

                                    DELETE_TASK_TYPE,
                                    GET_TASK_TYPE,
                                    EDIT_TASK_TYPE,
                                    ADD_TASK_TYPE,

                                    CREATE_SALARY,
                                    EDIT_SALARY,
                                    GET_SALARY,
                                    DELETE_SALARY,

                                    ADD_STAGE,
                                    EDIT_STAGE,
                                    GET_STAGE,
                                    DELETE_STAGE,

                                    DELETE_PROJECT,
                                    GET_PROJECT,
                                    EDIT_PROJECT,
                                    ADD_PROJECT,

                                    DELETE_BONUS,
                                    GET_BONUS,
                                    EDIT_BONUS,
                                    ADD_BONUS,

                                    DELETE_TASK,
                                    GET_TASK,
                                    EDIT_TASK,
                                    ADD_TASK,
                                    GET_OWN_TASK,

                                    ADD_PRIZE,
                                    VIEW_PRIZE,

                                    ADD_LESSON,
                                    VIEW_LESSON,
                                    VIEW_LESSON_ROLE,
                                    EDIT_LESSON,
                                    DELETE_LESSON,

                                    VIEW_INVOICE,
                                    EDIT_INVOICE,

                                    VIEW_LID,
                                    EDIT_LID,
                                    DELETE_LID,

                                    VIEW_FORM_LID,
                                    ADD_FORM_LID,
                                    EDIT_FORM_LID,
                                    DELETE_FORM_LID,

                                    VIEW_LID_STATUS,
                                    ADD_LID_STATUS,
                                    EDIT_LID_STATUS,
                                    DELETE_LID_STATUS,

                                    DELETE_JOB,
                                    EDIT_JOB,
                                    ADD_JOB,
                                    VIEW_JOB,

                                    DELETE_PROJECT_STATUS,
                                    GET_PROJECT_STATUS,
                                    EDIT_PROJECT_STATUS,
                                    ADD_PROJECT_STATUS,

                                    VIEW_ORG,
                                    ADD_WORK_TIME,
                                    GET_WORK_TIME,

                                    VIEW_BALANCE_HISTORY,
                                    VIEW_BALANCE,
                                    EDIT_BALANCE,
                                    ADD_BALANCE,

                                    VIEW_DASHBOARD,

                                    VIEW_NAVIGATION,
                                    DELETE_NAVIGATION,
                                    ADD_NAVIGATION,

                                    EDIT_MY_BUSINESS,
                                    VIEW_MY_BUSINESS,

                                    ADD_LOSS,
                                    GET_LOSS,
                                    EDIT_LOSS,
                                    DELETE_LOSS
                            ),
                            business));
            Role manager = roleRepository.save(new Role(
                    Constants.MANAGER,
                    Arrays.asList(
                            GET_TARIFF,

                            ADD_ADDRESS,
                            EDIT_ADDRESS,
                            VIEW_ADDRESS,
                            DELETE_ADDRESS,

                            UPLOAD_MEDIA,
                            DOWNLOAD_MEDIA,
                            VIEW_MEDIA_INFO,
                            DELETE_MEDIA,

                            ADD_BRAND,
                            EDIT_BRAND,
                            VIEW_BRAND,
                            DELETE_BRAND,

                            ADD_CATEGORY,
                            EDIT_CATEGORY,
                            VIEW_CATEGORY,
                            DELETE_CATEGORY,
                            ADD_CHILD_CATEGORY,

                            ADD_CURRENCY,
                            EDIT_CURRENCY,
                            VIEW_CURRENCY,
                            DELETE_CURRENCY,

                            ADD_CUSTOMER,
                            EDIT_CUSTOMER,
                            VIEW_CUSTOMER,
                            DELETE_CUSTOMER,

                            ADD_MEASUREMENT,
                            EDIT_MEASUREMENT,
                            VIEW_MEASUREMENT,
                            DELETE_MEASUREMENT,

                            ADD_OUTLAY,
                            EDIT_OUTLAY,
                            VIEW_OUTLAY,
                            DELETE_OUTLAY,

                            ADD_PRODUCT,
                            EDIT_PRODUCT,
                            VIEW_PRODUCT,
                            DELETE_PRODUCT,
                            VIEW_PRODUCT_ADMIN,

                            ADD_ROLE,
                            EDIT_ROLE,
                            VIEW_ROLE,
                            DELETE_ROLE,

                            ADD_SUPPLIER,
                            EDIT_SUPPLIER,
                            VIEW_SUPPLIER,
                            DELETE_SUPPLIER,

                            ADD_USER,
                            EDIT_USER,
                            VIEW_USER,
                            DELETE_USER,
                            EDIT_MY_PROFILE,

                            ADD_TRADE,
                            EDIT_TRADE,
                            VIEW_TRADE,
                            DELETE_TRADE,
                            DELETE_MY_TRADE,
                            VIEW_MY_TRADE,

                            ADD_TAX,
                            DELETE_TAX,
                            EDIT_TAX,
                            VIEW_TAX,

                            ADD_CUSTOMER_GROUP,
                            DELETE_CUSTOMER_GROUP,
                            EDIT_CUSTOMER_GROUP,
                            VIEW_CUSTOMER_GROUP,

                            ADD_PAY_METHOD,
                            EDIT_PAY_METHOD,
                            VIEW_PAY_METHOD,
                            DELETE_PAY_METHOD,

                            ADD_PAY_STATUS,
                            EDIT_PAY_STATUS,
                            VIEW_PAY_STATUS,
                            DELETE_PAY_STATUS,

                            ADD_PURCHASE,
                            EDIT_PURCHASE,
                            VIEW_PURCHASE,
                            DELETE_PURCHASE,

                            ADD_EXCHANGE,
                            EDIT_EXCHANGE,
                            VIEW_EXCHANGE,
                            DELETE_EXCHANGE,

                            VIEW_BENEFIT_AND_LOST,

                            ADD_PRODUCT_TYPE,
                            GET_PRODUCT_TYPE,
                            UPDATE_PRODUCT_TYPE,
                            DELETE_PRODUCT_TYPE,

                            GET_EXCEL,
                            POST_EXCEL,

                            VIEW_INFO,
                            VIEW_INFO_ADMIN,

                            GET_BUSINESS_ALL_AMOUNT,


                            CREATE_CONTENT,
                            EDIT_CONTENT,
                            GET_CONTENT,
                            DELETE_CONTENT,

                            CREATE_PRODUCTION,
                            GET_PRODUCTION,
                            VIEW_REPORT,

                            ADD_PROJECT_TYPE,
                            EDIT_PROJECT_TYPE,
                            GET_PROJECT_TYPE,
                            DELETE_PROJECT_TYPE,

                            DELETE_TASK_STATUS,
                            GET_TASK_STATUS,
                            EDIT_TASK_STATUS,
                            ADD_TASK_STATUS,

                            DELETE_TASK_TYPE,
                            GET_TASK_TYPE,
                            EDIT_TASK_TYPE,
                            ADD_TASK_TYPE,

                            ADD_STAGE,
                            EDIT_STAGE,
                            GET_STAGE,
                            DELETE_STAGE,

                            DELETE_PROJECT,
                            GET_PROJECT,
                            EDIT_PROJECT,
                            ADD_PROJECT,

                            DELETE_BONUS,
                            GET_BONUS,
                            EDIT_BONUS,
                            ADD_BONUS,

                            ADD_PRIZE,
                            VIEW_PRIZE,

                            ADD_LESSON,
                            VIEW_LESSON,
                            VIEW_LESSON_ROLE,
                            EDIT_LESSON,
                            DELETE_LESSON,

                            VIEW_INVOICE,
                            EDIT_INVOICE,

                            VIEW_LID,
                            EDIT_LID,
                            DELETE_LID,

                            VIEW_FORM_LID,
                            ADD_FORM_LID,
                            EDIT_FORM_LID,
                            DELETE_FORM_LID,

                            VIEW_LID_STATUS,
                            ADD_LID_STATUS,
                            EDIT_LID_STATUS,
                            DELETE_LID_STATUS,

                            DELETE_JOB,
                            EDIT_JOB,
                            ADD_JOB,
                            VIEW_JOB,

                            DELETE_PROJECT_STATUS,
                            GET_PROJECT_STATUS,
                            EDIT_PROJECT_STATUS,
                            ADD_PROJECT_STATUS,
                            ADD_WORK_TIME,
                            GET_WORK_TIME,

                            GET_OWN_TASK,

                            VIEW_DASHBOARD,

                            VIEW_NAVIGATION,
                            DELETE_NAVIGATION,
                            ADD_NAVIGATION
                    ),
                    business));

            Role employee = roleRepository.save(new Role(
                    Constants.EMPLOYEE,
                    Arrays.asList(UPLOAD_MEDIA,
                            DOWNLOAD_MEDIA,
                            VIEW_MEDIA_INFO,
                            VIEW_BRAND,
                            ADD_CURRENCY,
                            EDIT_CURRENCY,
                            VIEW_CURRENCY,
                            DELETE_CURRENCY,
                            ADD_MEASUREMENT,
                            EDIT_MEASUREMENT,
                            VIEW_MEASUREMENT,
                            DELETE_MEASUREMENT,

                            ADD_TRADE,
                            EDIT_TRADE,
                            VIEW_MY_TRADE,
                            DELETE_MY_TRADE,

                            ADD_PAY_METHOD,
                            EDIT_PAY_METHOD,
                            VIEW_PAY_METHOD,
                            DELETE_PAY_METHOD,
                            ADD_PAY_STATUS,
                            EDIT_PAY_STATUS,
                            VIEW_PAY_STATUS,
                            DELETE_PAY_STATUS,
                            EDIT_MY_PROFILE,
                            VIEW_PRODUCT,

                            CREATE_CONTENT,
                            EDIT_CONTENT,
                            GET_CONTENT,
                            DELETE_CONTENT,

                            CREATE_PRODUCTION,
                            GET_PRODUCTION,

                            GET_WORK_TIME,

                            VIEW_DASHBOARD

                    ),
                    business
            ));


            payMethodRepository.save(new PaymentMethod(
                    "Naqd",
                    business
            ));

            payMethodRepository.save(new PaymentMethod(
                    "PlastikKarta",
                    business
            ));

            payMethodRepository.save(new PaymentMethod(
                    "BankOrqali",
                    business
            ));

            currencyRepository.save(new Currency(
                    business,
                    11400
            ));

            Set<Branch> branches = new HashSet<>();
            Branch branch = branchRepository.save(new Branch(
                    "Test Filial",
                    address,
                    business
            ));
            invoiceService.create(branch);
            branches.add(branch);


            BusinessService.createProjectStatus(branch, projectStatusRepository);


            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setName("Completed");
            taskStatus.setOrginalName("Completed");
            taskStatus.setRowNumber(2);
            taskStatus.setABoolean(true);
            taskStatus.setColor("#04d227");
            taskStatus.setBranch(branch);
            taskStatusRepository.save(taskStatus);

            TaskStatus taskStatus2 = new TaskStatus();
            taskStatus2.setName("Uncompleted");
            taskStatus2.setOrginalName("Uncompleted");
            taskStatus2.setRowNumber(1);
            taskStatus2.setABoolean(true);
            taskStatus2.setColor("#FF0000");
            taskStatus2.setBranch(branch);
            taskStatusRepository.save(taskStatus2);

            User userAdmin = new User(
                    "Admin",
                    "Admin",
                    "admin",
                    passwordEncoder.encode("123"),
                    admin,
                    true,
                    business,
                    branches,
                    true
            );
            User user = new User(
                    "user",
                    "user",
                    "user",
                    passwordEncoder.encode("123"),
                    admin,
                    true,
                    business,
                    branches,
                    true
            );
            userRepository.save(user);

            userAdmin.setArrivalTime("08:00");
            userAdmin.setLeaveTime("16:00");
            userAdmin = userRepository.save(userAdmin);
            agreementService.add(userAdmin);

            User userSuperAdmin = new User(
                    "SuperAdmin",
                    "Admin of site",
                    "superadmin",
                    passwordEncoder.encode("admin123"),
                    superAdmin,
                    true,
                    business,
                    branches,
                    true
            );
            userSuperAdmin.setArrivalTime("08:00");
            userSuperAdmin.setLeaveTime("16:00");
            userSuperAdmin = userRepository.save(userSuperAdmin);
            agreementService.add(userSuperAdmin);

            User userManager = new User(
                    "Manager",
                    "manager",
                    "manager",
                    passwordEncoder.encode("manager123"),
                    manager,
                    true,
                    business,
                    branches,
                    true
            );
            userManager.setArrivalTime("08:00");
            userManager.setLeaveTime("16:00");
            userManager = userRepository.save(userManager);
            agreementService.add(userManager);

            User userEmployee = new User(
                    "Employee",
                    "employee",
                    "employee",
                    passwordEncoder.encode("employee123"),
                    employee,
                    true,
                    business,
                    branches,
                    true
            );
            userEmployee.setArrivalTime("08:00");
            userEmployee.setLeaveTime("16:00");
            userEmployee = userRepository.save(userEmployee);
            agreementService.add(userEmployee);


            paymentStatusRepository.save(new PaymentStatus(
                    TOLANGAN.name()
            ));

            paymentStatusRepository.save(new PaymentStatus(
                    QISMAN_TOLANGAN.name()
            ));

            paymentStatusRepository.save(new PaymentStatus(
                    TOLANMAGAN.name()
            ));

            List<PaymentMethod> newAll = payMethodRepository.findAll();
            for (PaymentMethod paymentMethod : newAll) {
                Balance balance = new Balance();
                balance.setAccountSumma(0);
                balance.setPaymentMethod(paymentMethod);
                for (Branch branch1 : branches) {
                    balance.setBranch(branch1);
                }
                balanceRepository.save(balance);
            }

            exchangeStatusRepository.save(new ExchangeStatus(
                    BUYURTMA_QILINGAN.name()
            ));

            exchangeStatusRepository.save(new ExchangeStatus(
                    KUTILMOQDA.name()
            ));

            exchangeStatusRepository.save(new ExchangeStatus(
                    QABUL_QILINGAN.name()
            ));
        } else if (initMode.equals("never")) {
            List<Business> businessRepositoryAll1 = businessRepository.findAll();
            for (Business business2 : businessRepositoryAll1) {
                List<Shablon> all1 = shablonRepository.findAllByBusiness_Id(business2.getId());
                if (all1.isEmpty()) {
                    Shablon shablon = new Shablon();
                    shablon.setName("Tug'ilgan kun uchun");
                    shablon.setOriginalName("bithday");
                    shablon.setMessage("Hurmatli {ism} tugilgan kuningiz bilan");
                    shablon.setBusiness(business2);
                    shablonRepository.save(shablon);

                    Shablon shablon2 = new Shablon();
                    shablon2.setName("Mijozlar qarzi");
                    shablon2.setOriginalName("debtCustomer");
                    shablon2.setMessage("Hurmatli mijoz qarzingiz bor");
                    shablon2.setBusiness(business2);
                    shablonRepository.save(shablon2);

                    Shablon shablon3 = new Shablon();
                    shablon3.setName("Task qo'shilganda");
                    shablon3.setOriginalName("newTask");
                    shablon3.setMessage("yangi task qoshildi");
                    shablon3.setBusiness(business2);
                    shablonRepository.save(shablon3);
                }
                List<LidStatus> rejection = lidStatusRepository.
                        findAllByBusinessIdAndOrginalName(business2.getId(), "Rejection");

                if (rejection.isEmpty()) {
                    LidStatus rejectionStatus = new LidStatus();
                    rejectionStatus.setName("Rejection");
                    rejectionStatus.setIncrease(true);
                    rejectionStatus.setOrginalName("Rejection");
                    rejectionStatus.setColor("rang");
                    Integer maxSort = lidStatusRepository.getMaxSort(business2.getId());
                    rejectionStatus.setSort(maxSort + 1);
                    rejectionStatus.setBusiness(business2);
                    lidStatusRepository.save(rejectionStatus);
                }
            }
//            updatePermission(); // TODO: 5/29/2023 if you add new permission

            /*List<User> allByRoleId = userRepository.findAllByRole_Id(roleRepository.findByName("Super Admin").get().getId());
            for (User user : allByRoleId) {
                if (user.getUsername().equals("superadmin")) {
                    user.setPassword(passwordEncoder.encode("dexqonchilik"));
                    userRepository.save(user);
                }
            }*/
        }
    }

    private void updatePermission() {
        List<Permissions> newPermissionList = Arrays.asList(// TODO: 5/29/2023 write new permissions here
        );
        Optional<Role> superAdmin = roleRepository.findByName(Constants.SUPERADMIN);
        List<Role> adminList = roleRepository.findAllByName(Constants.ADMIN);
        superAdmin.ifPresent(adminList::add);
        updatePermissionHelperRole(adminList, newPermissionList);
        updatePermissionHelperTariff(tariffRepository.findAll(), newPermissionList);
    }

    private void updatePermissionHelperRole(List<Role> roleList, List<Permissions> newPermissionList) {
        for (Role role : roleList) {
            List<Permissions> permissions = role.getPermissions();
            for (Permissions newPermission : newPermissionList) {
                if (!permissions.contains(newPermission))
                    permissions.add(newPermission);
            }
        }
        roleRepository.saveAll(roleList);
    }

    private void updatePermissionHelperTariff(List<Tariff> tariffList, List<Permissions> newPermissionList) {
        for (Permissions newPermission : newPermissionList) {
            for (Tariff tariff : tariffList) {
                if (!tariff.getPermissions().contains(newPermission))
                    tariff.getPermissions().add(newPermission);
            }
        }
        tariffRepository.saveAll(tariffList);
    }
}
