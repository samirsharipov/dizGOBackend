package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.helpers.CreateEntityHelper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BranchDto;
import uz.pdp.springsecurity.payload.BranchGetDto;
import uz.pdp.springsecurity.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final AddressRepository addressRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final BusinessRepository businessRepository;
    private final InvoiceService invoiceService;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final PayMethodRepository payMethodRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BranchCategoryRepository branchCategoryRepository;
    private final CreateEntityHelper createEntityHelper;
    private final LocationRepository locationRepository;

    public ApiResponse addBranch(BranchDto branchDto) {
        Branch branch = new Branch();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UUID businessId = branchDto.getBusinessId();

        Optional<Subscription> optionalSubscription = subscriptionRepository.findByBusinessIdAndActiveTrue(businessId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            int branchAmount = subscription.getTariff().getBranchAmount();
            int count = branchRepository.countAllByBusiness_Id(businessId);
            if (branchAmount != 0) {
                if (branchAmount <= count) {
                    return new ApiResponse("Tarif bo'yicha fillial qo'shish limiti cheklangan!", false);
                }
            }
        }

        branch.setName(branchDto.getName());

        Optional<Address> byId = addressRepository.findById(branchDto.getAddressId());
        if (byId.isEmpty()) return new ApiResponse("ADDRESS NOT FOUND", false);
        branch.setAddress(byId.get());

        Optional<Business> optionalBusiness = businessRepository.findById(branchDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);
        branch.setBusiness(optionalBusiness.get());

        branch.setMainBranchId(branchDto.getMainBranchId());
        branch.setAddressName(branchDto.getAddressName());

        if (user.getBusiness().getMain()) {
            if (branchDto.getCategoryId() != null) {
                boolean existed = branchRepository.existsByBranchCategory_Id(branchDto.getCategoryId());
                if (existed) {
                    return new ApiResponse("Bunday categoriya oldin boshqa fillialga boglangan", false);
                } else {
                    branchCategoryRepository.findById(branchDto.getCategoryId()).ifPresent(branch::setBranchCategory);
                }
            }
        }

        Branch save = branchRepository.save(branch);
        user.getBranches().add(branch);
        userRepository.save(user);
        invoiceService.create(branch);

        createTaskStatus(branch, taskStatusRepository);

        createBalance(branch, balanceRepository, payMethodRepository);

        createProjectStatus(branch);
        Location location = new Location();
        location.setBranchId(save.getId());
        location.setLatitude(branchDto.getLatitude());
        location.setLongitude(branchDto.getLongitude());
        location.setRadius(50);
        locationRepository.save(location);


        return new ApiResponse("Added", true);
    }

    private void createProjectStatus(Branch branch) {
        createEntityHelper.createProjectStatus(branch, projectStatusRepository);
    }

    static void createTaskStatus(Branch branch, TaskStatusRepository taskStatusRepository) {
        TaskStatus completedTaskStatus = new TaskStatus();
        completedTaskStatus.setName("Completed");
        completedTaskStatus.setOrginalName("Completed");
        completedTaskStatus.setRowNumber(2);
        completedTaskStatus.setABoolean(true);
        completedTaskStatus.setColor("#04d227");
        completedTaskStatus.setBranch(branch);
        taskStatusRepository.save(completedTaskStatus);

        TaskStatus uncompletedTaskStatus = new TaskStatus();
        uncompletedTaskStatus.setName("Uncompleted");
        uncompletedTaskStatus.setOrginalName("Uncompleted");
        uncompletedTaskStatus.setRowNumber(1);
        uncompletedTaskStatus.setABoolean(true);
        uncompletedTaskStatus.setColor("#FF0000");
        uncompletedTaskStatus.setBranch(branch);
        taskStatusRepository.save(uncompletedTaskStatus);
    }

    public static void createBalance(Branch branch, BalanceRepository balanceRepository, PayMethodRepository payMethodRepository) {
        List<PaymentMethod> allByBusinessId = payMethodRepository.findAll();

        for (PaymentMethod paymentMethod : allByBusinessId) {
            Balance balanceSum = new Balance();
            Balance balanceDollar = new Balance();
            balanceSum.setAccountSumma(0);
            balanceDollar.setAccountSumma(0);
            balanceSum.setBranch(branch);
            balanceDollar.setBranch(branch);
            balanceSum.setPaymentMethod(paymentMethod);
            balanceDollar.setPaymentMethod(paymentMethod);
            balanceDollar.setCurrency("DOLLAR");
            balanceSum.setCurrency("SOM");
            balanceRepository.save(balanceSum);
            balanceRepository.save(balanceDollar);
        }
    }

    public ApiResponse editBranch(UUID id, BranchDto branchDto) {
        if (!branchRepository.existsById(id)) return new ApiResponse("BRANCH NOT FOUND", false);

        Branch branch = branchRepository.getById(id);
        branch.setName(branchDto.getName());

        if (!addressRepository.existsById(branchDto.getAddressId())) return new ApiResponse("ADDRESS NOT FOUND", false);
        branch.setAddress(branch.getAddress());

        Optional<Business> optionalBusiness = businessRepository.findById(branchDto.getBusinessId());
        if (optionalBusiness.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);

        Business business = optionalBusiness.get();
        branch.setBusiness(business);

        branch.setMainBranchId(branchDto.getMainBranchId());

        Optional<Location> optionalLocation = locationRepository.findByBranchId(branch.getId());

        optionalLocation.ifPresent(location -> {
            location.setLatitude(branchDto.getLatitude());
            location.setLongitude(branchDto.getLongitude());
        });

        branch.setAddressName(branchDto.getAddressName());
        if (business.getMain()) {
            if (branchDto.getCategoryId() != null) {
                boolean exists = branchRepository.existsByBranchCategory_Id(branchDto.getCategoryId());
                if (exists && !branch.getBranchCategory().getId().equals(branchDto.getCategoryId())) {
                    return new ApiResponse("Bunday categoriya oldin boshqa fillialga boglangan", false);
                }
                branchCategoryRepository.findById(branchDto.getCategoryId()).ifPresent(branch::setBranchCategory);
            }
        }

        branchRepository.save(branch);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse getBranch(UUID id) {
        if (!branchRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, branchRepository.findById(id).get());
    }

    public ApiResponse deleteBranch(UUID id) {
        if (!branchRepository.existsById(id)) return new ApiResponse("NOT FOUND", false);

        branchRepository.deleteById(id);
        return new ApiResponse("DELETED", true);
    }

    public ApiResponse getByBusinessId(UUID business_id) {
        List<Branch> allByBusiness_id = branchRepository.findAllByBusiness_Id(business_id);
        if (allByBusiness_id.isEmpty()) return new ApiResponse("BUSINESS NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBusiness_id.stream().map(this::toDto).collect(Collectors.toList()));
    }

    public BranchGetDto toDto(Branch branch) {
        return new BranchGetDto(
                branch.getId(),
                branch.getName(),
                branch.getAddress() != null ? branch.getAddress().getId() : null,
                branch.getBranchCategory() != null ? branch.getBranchCategory().getId() : null,
                branch.getBranchCategory() != null ? branch.getBranchCategory().getName() : null
        );
    }

}
