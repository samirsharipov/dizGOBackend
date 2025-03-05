package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.Balance;
import uz.dizgo.erp.entity.BalanceHistory;
import uz.dizgo.erp.entity.PaymentMethod;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.BalanceDto;
import uz.dizgo.erp.payload.BalanceGetDto;
import uz.dizgo.erp.payload.PaymentDto;
import uz.dizgo.erp.repository.BalanceHistoryRepository;
import uz.dizgo.erp.repository.BalanceRepository;
import uz.dizgo.erp.repository.PayMethodRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository repository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final PayMethodRepository payMethodRepository;

    public ApiResponse edit(UUID branchId, Double summa, Boolean isPlus, UUID payMethodId, boolean dollar, String description) {
        Optional<Balance> optionalBalance;
        if (dollar) {
            optionalBalance = repository.findByPaymentMethod_IdAndBranchIdAndCurrencyIgnoreCase(payMethodId, branchId, "DOLLAR");
        } else {
            optionalBalance = repository.findByPaymentMethod_IdAndBranchIdAndCurrencyIgnoreCase(payMethodId, branchId, "SOM");
        }

        if (optionalBalance.isPresent()) {
            Balance balance = optionalBalance.get();
            if (summa > 0) {
                BalanceHistory newBalanceHistory = new BalanceHistory();
                newBalanceHistory.setBalance(balance);
                newBalanceHistory.setAccountSumma(balance.getAccountSumma());

                double totalSumma;
                if (Boolean.TRUE.equals(isPlus)) {
                    totalSumma = balance.getAccountSumma() + summa;
                } else {
                    totalSumma = balance.getAccountSumma() - summa;
                }
                balance.setAccountSumma(totalSumma);
                newBalanceHistory.setTotalSumma(totalSumma);
                newBalanceHistory.setPlus(isPlus);
                newBalanceHistory.setSumma(summa);
                newBalanceHistory.setDescription(description);
                newBalanceHistory.setCurrency(optionalBalance.get().getCurrency());

                balanceHistoryRepository.save(newBalanceHistory);
                repository.save(balance);

                return new ApiResponse("successfully saved", true);
            }
        }
        return new ApiResponse("not found balance", false);
    }

    public ApiResponse getAll(UUID branchId) {
        List<Balance> balanceList = repository.findAllByBranchId(branchId);
        if (balanceList.isEmpty()) {
            return new ApiResponse("not found balance by branch id", false);
        }

        List<BalanceDto> balanceDtoList = new ArrayList<>();
        for (Balance balance : balanceList) {
            BalanceDto balanceDto = new BalanceDto();
            balanceDto.setBalanceSumma(balance.getAccountSumma());
            balanceDto.setBranchName(balance.getBranch().getName());
            balanceDto.setBranchId(balance.getBranch().getId());
            balanceDto.setPayMethodName(balance.getPaymentMethod().getType());
            balanceDto.setPaymentMethodId(balance.getPaymentMethod().getId());
            balanceDto.setCurrency(balance.getCurrency());
            balanceDtoList.add(balanceDto);
        }

        balanceDtoList.sort(Comparator.comparing(BalanceDto::getCurrency));

        return new ApiResponse("found", true, balanceDtoList);
    }

    public ApiResponse edit(UUID branchId, boolean isPlus, List<PaymentDto> paymentDtoList, String dollar, String description) {
        for (PaymentDto paymentMethod : paymentDtoList) {
            Optional<Balance> optionalBalance = repository.findByPaymentMethod_IdAndBranchIdAndCurrencyIgnoreCase(paymentMethod.getPaymentMethodId(), branchId, dollar);

            if (optionalBalance.isPresent()) {
                Balance balance = optionalBalance.get();
                if ((paymentMethod.getIsDollar() != null && paymentMethod.getIsDollar() ? paymentMethod.getPaidSumDollar() : paymentMethod.getPaidSum()) > 0) {

                    BalanceHistory newBalanceHistory = new BalanceHistory();
                    newBalanceHistory.setBalance(balance);
                    newBalanceHistory.setAccountSumma(balance.getAccountSumma());

                    double totalSumma;
                    if (isPlus) {
                        totalSumma = balance.getAccountSumma() + (paymentMethod.getIsDollar() != null && paymentMethod.getIsDollar() ? paymentMethod.getPaidSumDollar() : paymentMethod.getPaidSum());
                    } else {
                        totalSumma = balance.getAccountSumma() - (paymentMethod.getIsDollar() != null && paymentMethod.getIsDollar() ? paymentMethod.getPaidSumDollar() : paymentMethod.getPaidSum());
                    }

                    balance.setAccountSumma(totalSumma);
                    newBalanceHistory.setTotalSumma(totalSumma);
                    newBalanceHistory.setPlus(isPlus);
                    newBalanceHistory.setDescription(description);
                    newBalanceHistory.setSumma((paymentMethod.getIsDollar() != null && paymentMethod.getIsDollar() ? paymentMethod.getPaidSumDollar() : paymentMethod.getPaidSum()));
                    newBalanceHistory.setCurrency(optionalBalance.get().getCurrency());

                    balanceHistoryRepository.save(newBalanceHistory);
                    repository.save(balance);

                    return new ApiResponse("successfully saved", true);
                }
                break;
            }
        }

        return new ApiResponse("Must not be a number less than 0", false);
    }

    public ApiResponse getAllByBusinessId(UUID businessId) {

        List<PaymentMethod> allPaymentMethod = payMethodRepository.findAll();
        List<BalanceGetDto> balanceGetDtoList = new ArrayList<>();

        for (PaymentMethod paymentMethod : allPaymentMethod) {
            List<Balance> all = repository.findAllByPaymentMethodIdAndCurrencyIgnoreCase(paymentMethod.getId(), "DOLLAR");
            getDtoList(balanceGetDtoList, all);
        }
        for (PaymentMethod paymentMethod : allPaymentMethod) {
            List<Balance> all = repository.findAllByPaymentMethodIdAndCurrencyIgnoreCase(paymentMethod.getId(), "SOM");
            getDtoList(balanceGetDtoList, all);
        }

        return new ApiResponse("all", true, balanceGetDtoList);
    }

    private void getDtoList(List<BalanceGetDto> balanceGetDtoList, List<Balance> all) {
        double totalSumma = 0;
        for (Balance balance : all) {
            BalanceGetDto balanceGetDto = new BalanceGetDto();
            totalSumma = totalSumma + balance.getAccountSumma();
            balanceGetDto.setPaymentMethodId(balance.getPaymentMethod().getId());
            balanceGetDto.setPayMethodName(balance.getPaymentMethod().getType());
            balanceGetDto.setBalanceSumma(totalSumma);
            balanceGetDto.setCurrency(balance.getCurrency());
            balanceGetDtoList.add(balanceGetDto);
        }
    }

    public ApiResponse getBalance(UUID businessId, UUID branchId) {
        List<BalanceGetDto> balanceGetDtoList = new ArrayList<>();

        if (branchId != null) {
            getDtoList(balanceGetDtoList, repository.findAllByBranchIdAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(branchId, "DOLLAR", "Naqd"));
            getDtoList(balanceGetDtoList, repository.findAllByBranchIdAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(branchId, "SOM", "Naqd"));
        } else {
            getDtoList(balanceGetDtoList, repository.findAllByBranch_Business_idAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(businessId, "DOLLAR", "Naqd"));
            getDtoList(balanceGetDtoList, repository.findAllByBranch_Business_idAndCurrencyIgnoreCaseAndPaymentMethod_TypeIgnoreCase(businessId, "SOM", "Naqd"));
        }

        return new ApiResponse("all", true, balanceGetDtoList);
    }
}
