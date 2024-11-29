package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
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
    private final PayMethodRepository payMethodRepository;
    private final NotificationRepository notificationRepository;
    private final ShablonRepository shablonRepository;

    private final BusinessHelper businessHelper;
    private final CreateEntityHelper createEntityHelper;

    private final static LocalDateTime TODAY = LocalDate.now().atStartOfDay();
    private final static LocalDateTime THIS_WEEK = TODAY.minusDays(TODAY.getDayOfWeek().ordinal());
    private final static LocalDateTime THIS_MONTH = LocalDateTime.of(TODAY.getYear(), TODAY.getMonth(), 1, 0, 0, 0);
    private final static LocalDateTime THIS_YEAR = LocalDateTime.of(TODAY.getYear(), 1, 1, 0, 0, 0);

    @Transactional
    public ApiResponse add(BusinessDto businessDto) {
        if (businessRepository.existsByNameIgnoreCase(businessDto.getName())) {
            return new ApiResponse("A BUSINESS WITH THAT NAME ALREADY EXISTS", false);
        }

        // Yangi biznes yaratish
        Business business = businessHelper.createNewBusiness(businessDto);

        // To'lov usullarini saqlash
        businessHelper.savePaymentMethods(business, payMethodRepository);

        // Obuna yaratish
        createEntityHelper.createSubscription(business, businessDto.getTariffId());

        // Manzil, Filial va Foydalanuvchi yaratish
        Address address = createEntityHelper.createAddress(businessDto.getAddressDto());
        Branch branch = createEntityHelper.createBranch(business, address, businessDto.getBranchDto());
        createEntityHelper.createBalance(branch);
        createEntityHelper.createAdminRoleAndUser(business, branch, businessDto);
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
                .map(business -> new ApiResponse("FOUND", true, business))
                .orElseGet(() -> new ApiResponse("Not found business", false));
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
        business.setStatus(Constants.ARCHIVED);
        businessRepository.save(business);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getAll() {
        return new ApiResponse("all business", true, businessMapper
                .toDtoList(businessRepository
                        .findAllByDeleteIsFalse()));
    }

    public ApiResponse deActive(UUID businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (optionalBusiness.isEmpty())
            new ApiResponse("not found business", false);

        Business business = optionalBusiness.get();
        business.setActive(false);
        business.setStatus(Constants.BLOCKED);
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