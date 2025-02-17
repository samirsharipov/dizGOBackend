package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.StatusName;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final BranchRepository branchRepository;
    private final BusinessRepository businessRepository;
    private final PurchaseRepository purchaseRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PayMethodRepository payMethodRepository;
    private final BalanceService balanceService;
    private final SupplierBalanceHistoryRepository supplierBalanceHistoryRepository;
    private final CustomerSupplierRepository customerSupplierRepository;
    private final CustomerSupplierService customerSupplierService;
    private final UserRepository userRepository;
    private final MessageService messageService;

    public ApiResponse add(SupplierDto supplierDto) {
        Business business = businessRepository.findById(supplierDto.getBusinessId())
                .orElseThrow(() -> new RuntimeException(messageService.getMessage("business.not.found")));

        Supplier supplier = setSupplier(supplierDto, business, new Supplier());
        supplierRepository.save(supplier);

        return new ApiResponse(messageService.getMessage("added.successfully"), true);
    }

    @Transactional
    public ApiResponse edit(UUID id, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("supplier.not.found"));

        setSupplier(supplierDto, supplier.getBusiness(), supplier);
        supplierRepository.save(supplier);

        return new ApiResponse(messageService.getMessage("edited.successfully"), true);
    }

    public ApiResponse get(UUID id) {
        return supplierRepository.findById(id)
                .map(supplier ->
                        new ApiResponse(messageService.getMessage("supplier.found"), true, convertToDto(supplier)))
                .orElseGet(() ->
                        new ApiResponse(messageService.getMessage("supplier.not.found"), false));
    }

    public ApiResponse delete(UUID id) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);

        if (supplierOptional.isEmpty()) {
            return new ApiResponse(messageService.getMessage("supplier.not.found"), false);
        }

        Supplier supplier = supplierOptional.get();
        supplier.setDeleted(true);
        supplier.setActive(false);
        supplierRepository.save(supplier);

        return new ApiResponse(messageService.getMessage("deleted.successfully"), true);
    }


    public ApiResponse getAllByBusiness(UUID businessId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Supplier> suppliers = supplierRepository.findAllByBusinessId(businessId, pageable);

        if (suppliers.isEmpty()) {
            return new ApiResponse(messageService.getMessage("supplier.not.found"), false);
        }

        List<SupplierGetDto> supplierGetDtoList = suppliers.getContent().stream()
                .map(this::convertToDto).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("suppliers", supplierGetDtoList);
        response.put("totalElements", suppliers.getTotalElements());
        response.put("totalPages", suppliers.getTotalPages());
        response.put("currentPage", suppliers.getNumber());
        response.put("pageSize", suppliers.getSize());

        return new ApiResponse(messageService.getMessage("supplier.found"), true, response);
    }

    @Transactional
    public ApiResponse storeRepayment(UUID id, RepaymentDto repaymentDto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(messageService.getMessage("supplier.not.found")));

        User user = userRepository.findById(repaymentDto.getUserId())
                .orElseThrow(() -> new RuntimeException(messageService.getMessage("user.not.found")));

        Double totalPaidSum = repaymentDto.getTotalPaidSum();
        if (totalPaidSum == null) {
            return new ApiResponse(messageService.getMessage("repayment.not.found"), false);
        }

        // Qarzni yangilash
        supplier.setDebt(supplier.getDebt() - totalPaidSum);
        supplierRepository.save(supplier);

        // Supplierga bog‘liq mijoz hisobini qayta hisoblash
        customerSupplierRepository.findBySupplierId(supplier.getId())
                .ifPresent(customerSupplierService::calculation);

        try {
            // To‘lovni bo‘lib-bo‘lib hisoblash
            setRepaymentHelper(totalPaidSum, repaymentDto.getPurchaseIdList());

            // Balansni yangilash
            balanceService.edit(
                    repaymentDto.getBranchId(), totalPaidSum, false,
                    repaymentDto.getPaymentMethodId(), false, "supplier"
            );

            // To‘lov usulini va filialni olish
            PaymentMethod paymentMethod = payMethodRepository.findById(repaymentDto.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException(messageService.getMessage("payment.method.not.found")));
            Branch branch = branchRepository.findById(repaymentDto.getBranchId())
                    .orElseThrow(() -> new RuntimeException(messageService.getMessage("branch.not.found")));

            // Supplier balans tarixini saqlash
            supplierBalanceHistoryRepository.save(new SupplierBalanceHistory(
                    totalPaidSum, paymentMethod, supplier,
                    repaymentDto.getDescription(), user, branch,
                    repaymentDto.getPurchaseIdList()
            ));

            return new ApiResponse(messageService.getMessage("repayment.stored"), true);
        } catch (Exception e) {
            return new ApiResponse(messageService.getMessage("error.message", new Object[]{e.getMessage()}), false);
        }
    }

    private void setRepaymentHelper(Double paidSum, List<UUID> purchaseIdList) {
        PaymentStatus paid = paymentStatusRepository.findByStatus(StatusName.TOLANGAN.name());
        PaymentStatus partiallyPaid = paymentStatusRepository.findByStatus(StatusName.QISMAN_TOLANGAN.name());
        List<Purchase> purchases = purchaseRepository.findAllById(purchaseIdList);

        // Purchases ro'yxatini createdAt bo'yicha asc tartibda saralash
        purchases.sort(Comparator.comparing(Purchase::getCreatedAt));

        for (Purchase purchase : purchases) {
            double debt = purchase.getDebtSum();
            double paidAmount = Math.min(paidSum, debt);

            purchase.setDebtSum(debt - paidAmount);
            purchase.setPaidSum(purchase.getPaidSum() + paidAmount);
            purchase.setPaymentStatus(purchase.getDebtSum() == 0 ? paid : partiallyPaid);

            paidSum -= paidAmount;
            if (paidSum == 0) break;
        }

        purchaseRepository.saveAll(purchases);
    }

    public ApiResponse supplierHistory(UUID id) {
        List<SupplierBalanceHistory> supplierBalanceHistories = supplierBalanceHistoryRepository.findAllBySupplierIdOrderByCreatedAtDesc(id);

        if (supplierBalanceHistories.isEmpty()) {
            return new ApiResponse(messageService.getMessage("supplier.history.not.found"), false);
        }

        List<SupplierHistory> histories = supplierBalanceHistories.stream()
                .map(supplierBalanceHistory -> new SupplierHistory(
                        supplierBalanceHistory.getId(),
                        supplierBalanceHistory.getPaymentMethod().getType(),
                        supplierBalanceHistory.getAmount(),
                        supplierBalanceHistory.getBranch().getName(),
                        supplierBalanceHistory.getUser().getFirstName(),
                        supplierBalanceHistory.getSupplier().getName(),
                        supplierBalanceHistory.getCreatedAt(),
                        supplierBalanceHistory.getDescription() != null ? supplierBalanceHistory.getDescription() : "Mavjud emas !",
                        supplierBalanceHistory.getPurchaseIdList()
                ))
                .collect(Collectors.toList());

        return new ApiResponse(messageService.getMessage("found"), true, histories);
    }

    public ApiResponse ourMoney(UUID businessId) {
        Map<String, Object> data = new HashMap<>();
        data.put("all_our_money", supplierRepository.allOurMoney(businessId));
        data.put("all_your_money", supplierRepository.allYourMoney(businessId));
        return new ApiResponse(true, data);
    }

    @NotNull
    private static Supplier setSupplier(SupplierDto supplierDto, Business business, Supplier supplier) {

        supplier.setName(supplierDto.getName());
        supplier.setPhoneNumber(supplierDto.getPhoneNumber());
        supplier.setTelegram(supplierDto.getTelegram());
        supplier.setDebt(supplierDto.getDebt());
        supplier.setBusiness(business);

        // Agar supplier juridical bo'lsa, qo'shimcha ma'lumotlarni o'rnatish
        if (supplierDto.isJuridical()) {
            supplier.setJuridical(true);
            supplier.setInn(supplierDto.getInn());
            supplier.setCompanyName(supplierDto.getCompanyName());
        }
        return supplier;
    }

    private SupplierGetDto convertToDto(Supplier supplier) {
        return new SupplierGetDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getPhoneNumber(),
                supplier.isJuridical(),
                supplier.getInn(),
                supplier.getBusiness().getId(),
                supplier.getDebt()
        );
    }
}