package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.*;
import uz.pdp.springsecurity.enums.StatusName;
import uz.pdp.springsecurity.payload.*;
import uz.pdp.springsecurity.repository.*;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.*;

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
//    private final CustomerService customerService;

    public ApiResponse add(SupplierDto supplierDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(supplierDto.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            return new ApiResponse("BUSINESS NOT FOUND", false);
        }
        Supplier supplier = new Supplier(
                supplierDto.getName(),
                supplierDto.getPhoneNumber(),
                supplierDto.getTelegram(),
                optionalBusiness.get()
        );

        if (supplierDto.isJuridical()) {
            supplier.setJuridical(true);
            supplier.setInn(supplierDto.getInn());
            supplier.setCompanyName(supplierDto.getCompanyName());
        }
        supplier.setDebt(supplierDto.getDebt());
        supplierRepository.save(supplier);

        if (supplierDto.getCustomerDto()!=null) {
            CustomerDto customerDto = supplierDto.getCustomerDto();
//            customerDto.setName(supplierDto.getName());
            customerDto.setPhoneNumber(supplierDto.getPhoneNumber());
//            customerDto.setTelegram(supplierDto.getTelegram());
//            ApiResponse apiResponse = customerService.add(customerDto);
            CustomerSupplier customerSupplier = new CustomerSupplier();
            customerSupplier.setSupplier(supplier);
//            customerSupplier.setCustomer((Customer) apiResponse.getObject());
            customerSupplierRepository.save(customerSupplier);
        }

        return new ApiResponse("ADDED", true);
    }

    public ApiResponse edit(UUID id, SupplierDto supplierDto) {
        if (!supplierRepository.existsById(id)) return new ApiResponse("supplier NOT FOUND", false);

        Supplier supplier = supplierRepository.getById(id);
        supplier.setName(supplierDto.getName());
        supplier.setPhoneNumber(supplierDto.getPhoneNumber());
        supplier.setTelegram(supplierDto.getTelegram());

        if (supplierDto.isJuridical()) {
            supplier.setJuridical(true);
            supplier.setInn(supplierDto.getInn());
            supplier.setCompanyName(supplierDto.getCompanyName());
        }


        supplier.setDebt(supplierDto.getDebt());
        supplierRepository.save(supplier);
        return new ApiResponse("EDITED", true);
    }

    public ApiResponse get(UUID id) {
        if (!supplierRepository.existsById(id)) return new ApiResponse("SUPPLIER NOT FOUND", false);
        return new ApiResponse("FOUND", true, supplierRepository.findById(id).get());
    }

    public ApiResponse delete(UUID id) {
        if (!supplierRepository.existsById(id)) return new ApiResponse("SUPPLIER NOT FOUND", false);
        try {
            supplierRepository.deleteById(id);
            return new ApiResponse("DELETED", true);
        } catch (Exception e) {
            return new ApiResponse("TAMINOTCHI BILAN BOG'LIQ MA'LUMOTLAR MAVJUD", false);
        }
    }


    public ApiResponse getAllByBusiness(UUID businessId) {
        List<Supplier> allByBusinessId = supplierRepository.findAllByBusinessId(businessId);
        if (allByBusinessId.isEmpty()) return new ApiResponse("NOT FOUND", false);
        return new ApiResponse("FOUND", true, allByBusinessId);
    }

    @Transactional
    public ApiResponse storeRepayment(UUID id, RepaymentDto repaymentDto, User user) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isEmpty()) return new ApiResponse("Not Found Supplier", false);
        Supplier supplier = supplierOptional.get();
        if (repaymentDto.getRepayment() != null) {
            supplier.setDebt(supplier.getDebt() - repaymentDto.getRepayment());
            Supplier save = supplierRepository.save(supplier);
            Optional<CustomerSupplier> optionalCustomerSupplier = customerSupplierRepository.findBySupplierId(supplier.getId());
            optionalCustomerSupplier.ifPresent(customerSupplierService::calculation);
            try {
                storeRepaymentHelper(repaymentDto.getRepayment(), supplier);
                balanceService.edit(repaymentDto.getBranchId(), repaymentDto.getRepayment(), false, repaymentDto.getPaymentMethodId(), repaymentDto.getIsDollar(),"supplier");
                supplierBalanceHistoryRepository.save(new SupplierBalanceHistory(
                        repaymentDto.getRepayment(),
                        payMethodRepository.findById(repaymentDto.getPaymentMethodId()).orElseThrow(),
                        save,
                        repaymentDto.getDescription(),
                        user,
                        branchRepository.findById(repaymentDto.getBranchId()).orElseThrow()
                ));
                return new ApiResponse("Repayment Store !", true);
            } catch (Exception e) {
                return new ApiResponse("ERROR", false);
            }
        } else {
            return new ApiResponse("Repayment Not Found !", false);
        }
    }

    private void storeRepaymentHelper(Double paidSum, Supplier supplier) {
        PaymentStatus tolangan = paymentStatusRepository.findByStatus(StatusName.TOLANGAN.name());
        PaymentStatus qisman_tolangan = paymentStatusRepository.findByStatus(StatusName.QISMAN_TOLANGAN.name());
        List<Purchase> purchaseList = purchaseRepository.findAllBySupplierId(supplier.getId());
        for (Purchase purchase : purchaseList) {
            if (paidSum > purchase.getDebtSum()) {
                paidSum -= purchase.getDebtSum();
                purchase.setDebtSum(0);
                purchase.setPaidSum(purchase.getTotalSum());
                purchase.setPaymentStatus(tolangan);
            } else if (paidSum == purchase.getDebtSum()) {
                purchase.setDebtSum(0);
                purchase.setPaidSum(purchase.getTotalSum());
                purchase.setPaymentStatus(tolangan);
                break;
            } else {
                purchase.setDebtSum(purchase.getDebtSum() - paidSum);
                purchase.setPaidSum(purchase.getPaidSum() + paidSum);
                purchase.setPaymentStatus(qisman_tolangan);
                break;
            }
        }
        purchaseRepository.saveAll(purchaseList);
    }

    public ApiResponse supplierHistory(UUID id) {
        List<SupplierHistory> histories = new LinkedList<>();
        for (SupplierBalanceHistory supplierBalanceHistory : supplierBalanceHistoryRepository.findAllBySupplierIdOrderByCreatedAtDesc(id)) {
            histories.add(new SupplierHistory(
                    supplierBalanceHistory.getId(),
                    supplierBalanceHistory.getPaymentMethod().getType(),
                    supplierBalanceHistory.getAmount(),
                    supplierBalanceHistory.getBranch().getName(),
                    supplierBalanceHistory.getUser().getFirstName(),
                    supplierBalanceHistory.getSupplier().getName(),
                    supplierBalanceHistory.getCreatedAt(),
                    supplierBalanceHistory.getDescription() == null ? "Mavjud emas !" : supplierBalanceHistory.getDescription()
            ));
        }
        return new ApiResponse("Taminotchi istoriyasi", true, histories);
    }

    public ApiResponse ourMoney(UUID businessId) {
        Double allOurMoney = supplierRepository.allOurMoney(businessId);
        Double allYourMoney = supplierRepository.allYourMoney(businessId);
        Map<String, Object> data = new HashMap<>();
        data.put("all_our_money", allOurMoney);
        data.put("all_your_money", allYourMoney);
        return new ApiResponse(true, data);
    }
}
