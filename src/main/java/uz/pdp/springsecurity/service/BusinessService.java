package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.*;
import uz.pdp.springsecurity.helpers.BusinessHelper;
import uz.pdp.springsecurity.helpers.CreateEntityHelper;
import uz.pdp.springsecurity.mapper.BusinessMapper;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;
import uz.pdp.springsecurity.utils.Constants;

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
    private final SubscriptionRepository subscriptionRepository;
    private final BusinessMapper businessMapper;
    private final NotificationRepository notificationRepository;
    private final ShablonRepository shablonRepository;
    private final BranchRepository branchRepository;
    private final BusinessHelper businessHelper;
    private final CreateEntityHelper createEntityHelper;

    private final static LocalDateTime TODAY = LocalDate.now().atStartOfDay();
    private final static LocalDateTime THIS_WEEK = TODAY.minusDays(TODAY.getDayOfWeek().ordinal());
    private final static LocalDateTime THIS_MONTH = LocalDateTime.of(TODAY.getYear(), TODAY.getMonth(), 1, 0, 0, 0);
    private final static LocalDateTime THIS_YEAR = LocalDateTime.of(TODAY.getYear(), 1, 1, 0, 0, 0);
    private final AddressRepository addressRepository;

    @Transactional
    public ApiResponse add(BusinessDto businessDto) {

        ApiResponse apiResponse = checkValidations(businessDto);
        if (apiResponse != null) return apiResponse;

        // Yangi biznes yaratish
        Business business = businessRepository.save(businessHelper.createNewBusiness(businessDto));

        // Obuna yaratish
        createEntityHelper.createSubscription(business, businessDto.getTariffId());

        // Manzil, Filial va Foydalanuvchi yaratish
        Address address = new Address();
        Optional<Address> optionalAddress = addressRepository.findById(businessDto.getAddressId());
        if (optionalAddress.isPresent()) {
            address = optionalAddress.get();
        }

        BranchDto branchDto = new BranchDto();
        branchDto.setName(businessDto.getName());

        try {
            Optional<Branch> optionalBranch = branchRepository.findByBranchCategory_Id(businessDto.getBranchCategoryId());
            if (optionalBranch.isEmpty()) {
                return new ApiResponse("branch category does not exist", false);
            }
            Branch branch = optionalBranch.get();
            branchDto.setMainBranchId(branch.getId());
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }


        Branch branch = createEntityHelper.createBranch(business, address, branchDto);
        createEntityHelper.createBalance(branch);

        // User Role yaratish
        createEntityHelper.createAdminRoleAndUser(business, branch, businessDto);
        createEntityHelper.saveRole(business, roleRepository);

        createEntityHelper.createShablon(business, shablonRepository);

        // Qo'shimcha ma'lumotlar yaratish
        businessHelper.createStatusAndOther(business);

        // SuperAdmin'ga xabar yuborish
        notifySuperAdmin(business);

        // Filialga Task va Project Status qo'shish
        BranchService.createTaskStatus(branch, taskStatusRepository);
        createEntityHelper.createProjectStatus(branch, projectStatusRepository);


        return new ApiResponse("Successfully added", true);
    }

    @Nullable
    private ApiResponse checkValidations(BusinessDto businessDto) {
        if (businessRepository.existsByNameIgnoreCase(businessDto.getName())) {
            return new ApiResponse("A BUSINESS WITH THAT NAME ALREADY EXISTS", false);
        }
        if (businessRepository.existsByBusinessNumberIgnoreCase(businessDto.getBusinessNumber())) {
            return new ApiResponse("A BUSINESS WITH THAT NUMBER ALREADY EXISTS", false);
        }
        if (userRepository.existsByUsernameIgnoreCase(businessDto.getUserDto().getUsername())) {
            return new ApiResponse("A BUSINESS WITH USER ALREADY EXISTS", false);
        }
        return null;
    }


    private void notifySuperAdmin(Business business) {
        userRepository.findByUsername("superAdmin").ifPresent(superAdmin -> {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setName("Yangi biznes qo'shildi!");
            notification.setMessage("Yangi User va biznes qo'shildi, biznes tarifini aktivlashtirishingiz mumkin!");
            notification.setUserTo(superAdmin);
            notification.setType(NotificationType.NEW_BUSINESS);
            notification.setObjectId(business.getId());
            notificationRepository.save(notification);
        });
    }

    public ApiResponse edit(UUID id, BusinessEditDto businessEditDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty())
            return new ApiResponse("BUSINESS NOT FOUND", false);

        Optional<Business> businessOptional = businessRepository.findByName(businessEditDto.getName());
        if (businessOptional.isPresent()) {
            if (!businessOptional.get().getId().equals(id)) {
                return new ApiResponse("A BUSINESS WITH THAT NAME ALREADY EXISTS", false);
            }
        }
        Optional<Business> optional = businessRepository.findByBusinessNumber(businessEditDto.getBusinessNumber());
        if (optional.isPresent()) {
            if (!optional.get().getId().equals(id)) {
                return new ApiResponse("BUSINESS WITH THAT NUMBER ALREADY EXISTS", false);
            }
        }

        Business business = optionalBusiness.get();
        business.setName(businessEditDto.getName());
        business.setDescription(businessEditDto.getDescription());
        business.setActive(businessEditDto.isActive());
        business.setStatus(businessEditDto.getStatus());
        business.setBusinessNumber(businessEditDto.getBusinessNumber());

        businessRepository.save(business);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse getOne(UUID id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        return optionalBusiness
                .map(business -> new ApiResponse("FOUND", true, businessMapper.toGetOneDto(business)))
                .orElseGet(() -> new ApiResponse("Not found business", false));
    }


    public ApiResponse getAllPartners() {
        Optional<Role> optionalRole = roleRepository.findByName(Constants.SUPER_ADMIN);
        if (optionalRole.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Role superAdmin = optionalRole.get();

        Optional<Role> optionalAdmin = roleRepository.findByNameAndBusinessId(Constants.ADMIN, superAdmin.getBusiness().getId());
        if (optionalAdmin.isEmpty()) return new ApiResponse("NOT FOUND", false);
        Role admin = optionalAdmin.get();

        List<User> userList = userRepository.findAllByRole_IdAndBusiness_Deleted(admin.getId(), false);
        if (userList.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, userList);
    }

    public ApiResponse deleteOne(UUID id) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("not found business", false);
        }
        Business business = optionalBusiness.get();
        if (!business.isDeleted()) {
            business.setDeleted(true);
            business.setActive(false);
            business.setStatus(Constants.ARCHIVED);
        } else {
            business.setDeleted(false);
            business.setActive(true);
            business.setStatus(Constants.ACTIVE);
        }
        businessRepository.save(business);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAll() {

        List<BusinessGetDto> businessGetDtoList = new ArrayList<>();
        List<Business> all = businessRepository.findAll();
        for (Business business : all) {
            BusinessGetDto businessGetDto = new BusinessGetDto();
            businessGetDto.setId(business.getId());
            businessGetDto.setName(business.getName());
            businessGetDto.setDescription(business.getDescription());
            businessGetDto.setActive(business.isActive());
            businessGetDto.setCreatedAt(business.getCreatedAt());
            businessGetDto.setUpdateAt(business.getUpdateAt());
            businessGetDto.setContractStartDate(business.getContractStartDate());
            businessGetDto.setContractEndDate(business.getContractEndDate());
            List<Branch> branches = branchRepository.findAllByBusiness_Id(business.getId());
            for (Branch branch : branches) {
                if (branch.getMainBranchId() != null) {
                    Optional<Branch> optionalBranch = branchRepository.findById(branch.getMainBranchId());
                    if (optionalBranch.isPresent()) {
                        Branch mainBranch = optionalBranch.get();
                        BranchCategory branchCategory = mainBranch.getBranchCategory();
                        businessGetDto.setBusinessCategoryName(branchCategory != null ? branchCategory.getName() : mainBranch.getName() + " catgeroysi mavjud bomagan branchga boglangan");
                        businessGetDto.setBusinessCategoryId(branchCategory != null ? branchCategory.getId() : mainBranch.getId());
                        break;
                    }
                }
            }
            businessGetDtoList.add(businessGetDto);
        }
        return new ApiResponse("all business", true, businessGetDtoList);
    }

    public ApiResponse deActive(UUID businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty())
            new ApiResponse("not found business", false);

        Business business = optionalBusiness.get();
        if (business.isActive()) {
            business.setActive(false);
            business.setStatus(Constants.BLOCKED);
            userRepository.findAllByBusiness_Id(businessId).forEach(u -> {
                u.setEnabled(false);
                userRepository.save(u);
            });

            Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveTrue(businessId);
            if (optionalSubscription.isPresent()) {
                Subscription subscription = optionalSubscription.get();
                subscription.setActive(false);
                subscription.setDeleted(true);
                subscriptionRepository.save(subscription);
            }
        } else {
            business.setActive(true);
            business.setStatus(Constants.ACTIVE);
            userRepository.findAllByBusiness_Id(businessId).forEach(u -> {
                if (u.isActive() && !u.isEnabled()) {
                    u.setEnabled(true);
                    userRepository.save(u);
                }
            });
            Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveFalse(businessId);
            if (optionalSubscription.isPresent()) {
                Subscription subscription = optionalSubscription.get();
                subscription.setActive(true);
                subscription.setDeleted(false);
                subscriptionRepository.save(subscription);
            }
        }
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

    public ApiResponse getAllArchive() {
        return new ApiResponse("all business", true, businessMapper
                .toDtoList(businessRepository
                        .findAllByDeletedIsTrue()));
    }
}