package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.*;
import uz.pdp.springsecurity.mapper.AddressMapper;
import uz.pdp.springsecurity.mapper.BranchMapper;
import uz.pdp.springsecurity.mapper.BusinessMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.util.Constants;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final UserService userService;
    private final BranchRepository branchRepository;
    private final AddressRepository addressRepository;
    private final BranchMapper branchMapper;
    private final AddressMapper addressMapper;
    private final SubscriptionRepository subscriptionRepository;
    private final BusinessMapper businessMapper;
    private final PayMethodRepository payMethodRepository;
    private final NotificationRepository notificationRepository;
    private final LidStatusRepository lidStatusRepository;
    private final SourceRepository sourceRepository;
    private final LidFieldRepository lidFieldRepository;
    private final BalanceRepository balanceRepository;
    private final SmsService smsService;
    private  final ShablonRepository shablonRepository;

    private final static LocalDateTime TODAY = LocalDate.now().atStartOfDay();
    private final static LocalDateTime THIS_WEEK = TODAY.minusDays(TODAY.getDayOfWeek().ordinal());
    private final static LocalDateTime THIS_MONTH = LocalDateTime.of(TODAY.getYear(), TODAY.getMonth(), 1, 0, 0, 0);
    private final static LocalDateTime THIS_YEAR = LocalDateTime.of(TODAY.getYear(), 1, 1, 0, 0, 0);

    @Transactional
    public ApiResponse add(BusinessDto businessDto) {
        if (businessRepository.existsByNameIgnoreCase(businessDto.getName()))
            return new ApiResponse("A BUSINESS WITH THAT NAME ALREADY EXISTS", false);
        Business business = new Business();
        business.setName(businessDto.getName());
        business.setDescription(businessDto.getDescription());
        UUID tariffId = businessDto.getTariffId();
        Optional<Tariff> optionalTariff = tariffRepository.findById(tariffId);
        business.setActive(businessDto.isActive());
        business.setDelete(false);
        business = businessRepository.save(business);

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
        payMethodRepository.save(new PaymentMethod(
                "Mijoz_balance",
                business
        ));

        Subscription subscription = new Subscription();

        subscription.setBusiness(business);
        optionalTariff.ifPresent(subscription::setTariff);
        subscription.setActive(false);
        subscription.setStatusTariff(StatusTariff.WAITING);
        subscriptionRepository.save(subscription);


        AddressDto addressDto = businessDto.getAddressDto();
        BranchDto branchDto = businessDto.getBranchDto();
        UserDto userDto = businessDto.getUserDto();

        Address address = addressRepository.save(addressMapper.toEntity(addressDto));

        createStatusAndOther(business);

        branchDto.setAddressId(address.getId());
        branchDto.setBusinessId(business.getId());
        Branch branch = branchRepository.save(branchMapper.toEntity(branchDto));
        Set<UUID> branchIds = new HashSet<>();
        branchIds.add(branch.getId());
        userDto.setBranchId(branchIds);

        BranchService.createBalance(branch, balanceRepository, payMethodRepository);


        Role admin = new Role();
        admin.setName(Constants.ADMIN);
        admin.setPermissions(businessDto.getPermissionsList());
        admin.setBusiness(business);
        Role newRole = roleRepository.save(admin);

        userDto.setRoleId(newRole.getId());
        userDto.setBusinessId(business.getId());

        userService.add(userDto, true);

        Optional<User> superAdmin = userRepository.findByUsername("superAdmin");

        if (superAdmin.isPresent()) {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setName("Yangi bizness qo'shildi!");
            notification.setMessage("Yangi User va bizness qo'shildi biznes tarifini aktivlashtishingiz mumkin!");
            notification.setUserTo(superAdmin.get());
            notification.setType(NotificationType.NEW_BUSINESS);
            notification.setObjectId(business.getId());
            notificationRepository.save(notification);
        }

        BranchService.createTaskStatus(branch, taskStatusRepository);

        createProjectStatus(branch);

//        try {
//            smsService.createBusiness(business);
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage());
//        }

        return new ApiResponse("ADDED", true);
    }

    private void createProjectStatus(Branch branch) {
        createProjectStatus(branch, projectStatusRepository);
    }

    public static void createProjectStatus(Branch branch, ProjectStatusRepository projectStatusRepository) {
        ProjectStatus projectStatus1 = new ProjectStatus();
        projectStatus1.setName("Uncompleted");
        projectStatus1.setColor("red");
        projectStatus1.setBranch(branch);
        projectStatusRepository.save(projectStatus1);

        ProjectStatus projectStatus2 = new ProjectStatus();
        projectStatus2.setColor("yellow");
        projectStatus2.setName("Process");
        projectStatus2.setBranch(branch);
        projectStatusRepository.save(projectStatus2);

        ProjectStatus projectStatus3 = new ProjectStatus();
        projectStatus3.setColor("green");
        projectStatus3.setName("Completed");
        projectStatus3.setBranch(branch);
        projectStatusRepository.save(projectStatus3);
    }

    private void createStatusAndOther(Business business) {
        LidField lidField = new LidField();
        lidField.setName("FIO");
        lidField.setBusiness(business);
        lidField.setValueType(ValueType.STRING);
        lidField.setTanlangan(false);
        lidFieldRepository.save(lidField);

        LidField lidField1 = new LidField();
        lidField1.setName("Phone number");
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

        addLidStatus(business);
    }

    private void addLidStatus(Business business) {
        LidStatus newStatus = new LidStatus();
        newStatus.setName("New");
        newStatus.setIncrease(true);
        newStatus.setColor("rang");
        newStatus.setSort(1);
        newStatus.setBusiness(business);
        lidStatusRepository.save(newStatus);

        LidStatus progressStatus = new LidStatus();
        progressStatus.setName("Progress");
        progressStatus.setIncrease(true);
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


        Shablon shablon = new Shablon();
        shablon.setName("Tug'ilgan kun uchun");
        shablon.setOriginalName("bithday");
        shablon.setMessage("Hurmatli {ism} tugilgan kuningiz bilan");
        shablon.setBusiness(business);
        shablonRepository.save(shablon);

        Shablon shablon2 = new Shablon();
        shablon2.setName("Mijozlar qarzi");
        shablon2.setOriginalName("debtCustomer");
        shablon2.setMessage("Hurmatli mijoz qarzingiz bor");
        shablon2.setBusiness(business);
        shablonRepository.save(shablon2);

        Shablon shablon3 = new Shablon();
        shablon3.setName("Task qo'shilganda");
        shablon3.setOriginalName("newTask");
        shablon3.setMessage("yangi task qoshildi");
        shablon3.setBusiness(business);
        shablonRepository.save(shablon3);

    }

    public ApiResponse edit(UUID id, BusinessEditDto businessEditDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        Optional<Business> businessOptional = businessRepository.findByName(businessEditDto.getName());
        if (businessOptional.isPresent()) {
            if (!businessOptional.get().getId().equals(id)) {
                return new ApiResponse("A BUSINESS WITH THAT NAME ALREADY EXISTS", false);
            }
        }

        Business business = optionalBusiness.get();
        business.setName(businessEditDto.getName());
        business.setDescription(businessEditDto.getDescription());
//        business.setSaleMinus(businessEditDto.isSaleMinus());
        business.setActive(businessEditDto.isActive());

        businessRepository.save(business);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse getOne(UUID id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        return optionalBusiness.map(business -> new ApiResponse("FOUND", true, business)).orElseGet(() -> new ApiResponse("not found business", false));
    }


    public ApiResponse getAllPartners() {
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPERADMIN);
        if (optionalRole.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Role superAdmin = optionalRole.get();

        Optional<Role> optionalAdmin = roleRepository.findByNameAndBusinessId(Constants.ADMIN, superAdmin.getBusiness().getId());
        if (optionalAdmin.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Role admin = optionalAdmin.get();

        List<User> userList = userRepository.findAllByRole_IdAndBusiness_Delete(admin.getId(), false);
        if (userList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, userList);
    }

    public ApiResponse deleteOne(UUID id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }
        Business business = optionalBusiness.get();
        business.setDelete(true);
        business.setActive(false);
        businessRepository.save(business);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAll() {
        List<Business> all = businessRepository.findAllByDeleteIsFalse();
        return new ApiResponse("all business", true, businessMapper.toDtoList(all));
    }

    public ApiResponse deActive(UUID businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) new ApiResponse("not found business", false);
        Business business = optionalBusiness.get();
        business.setActive(!business.isActive());
        businessRepository.save(business);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse saleMinus(UUID businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) new ApiResponse("not found business", false);
        Business business = optionalBusiness.get();
        business.setSaleMinus(!business.isSaleMinus());
        businessRepository.save(business);
        return new ApiResponse("SUCCESS", true);
    }

    public ApiResponse getInfo(String time) {
        // "day" ni doim qabul qiladi
        Timestamp startTime = Timestamp.valueOf(TODAY);
        if (time.equals("THIS_WEEK")) {
            startTime = Timestamp.valueOf(THIS_WEEK);
        } else if (time.equals("THIS_MONTH")) {
            startTime = Timestamp.valueOf(THIS_MONTH);
        } else if (time.equals("THIS_YEAR")) {
            startTime = Timestamp.valueOf(THIS_YEAR);
        }

        Integer subscribers = businessRepository.countAllByCreatedAtAfter(startTime);

        List<Subscription> subscriptionList = subscriptionRepository.findAllByCreatedAtAfterAndStatusTariff(startTime, StatusTariff.CONFIRMED);
        double subscriptionPayment = 0d;
        for (Subscription subscription : subscriptionList) {
            subscriptionPayment += subscription.getTariff().getPrice();
        }

        Integer waiting = subscriptionRepository.countAllByStatusTariff(StatusTariff.WAITING);

        Integer rejected = subscriptionRepository.countAllByStatusTariff(StatusTariff.REJECTED);

        SuperAdminInfoDto infoDto = new SuperAdminInfoDto(
                subscribers, rejected, waiting, subscriptionPayment
        );

        return new ApiResponse(true, infoDto);
    }

    public ApiResponse checkBusinessName(CheckDto checkDto) {
        boolean exists = businessRepository.existsByNameIgnoreCase(checkDto.getCheckName());
        if (exists) return new ApiResponse("EXIST", false);
        return new ApiResponse("NOT FOUND FOUND", true);
    }

    public ApiResponse checkUsername(CheckDto checkDto) {
        boolean exists = userRepository.existsByUsernameIgnoreCase(checkDto.getCheckName());
        if (exists) return new ApiResponse("EXIST", false);
        return new ApiResponse("NOT FOUND FOUND", true);
    }

    public ApiResponse turnExchangeProduct(UUID businessId, boolean isTurn) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found", false);
        }

        Business business = optionalBusiness.get();
        business.setExchangeProductByConfirmation(isTurn);
        businessRepository.save(business);
        return new ApiResponse("successfully", true);
    }

    public ApiResponse getMyAllBusiness(UUID userId) {
        List<Business> allByDeleteIsFalse = businessRepository.findAllByDeleteIsFalseMyFunc(userId);
        return new ApiResponse("successfully", true, allByDeleteIsFalse);
    }

    public ApiResponse changeProductCheapSellingPrice(UUID cheapSellingPrice, Boolean checked) {
        Business business = businessRepository.findById(cheapSellingPrice).get();
        business.setCheapSellingPrice(checked);
        businessRepository.save(business);
        return new ApiResponse("successfully", true);
    }

    public ApiResponse changeGrossSell(UUID cheapSellingPrice, Boolean checked) {
        Business business = businessRepository.findById(cheapSellingPrice).get();
        business.setGrossPriceControl(checked);
        businessRepository.save(business);
        return new ApiResponse("successfully", true);
    }

    public int getEditDays(UUID businessId) {
        return businessRepository.findById(businessId)
                .map(Business::getEditDays)
                .orElse(30); // topilmasa 30 kunni qaytaradi
    }

    public void updateEditDays(UUID businessId, int editDays) {
        businessRepository.findById(businessId).ifPresent(business -> {
            business.setEditDays(editDays);
            businessRepository.save(business);
        });
    }
}
