package uz.pdp.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.entity.Balance;
import uz.pdp.springsecurity.entity.BalanceHistory;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.BalanceDto;
import uz.pdp.springsecurity.payload.BalanceGetDto;
import uz.pdp.springsecurity.payload.PaymentDto;
import uz.pdp.springsecurity.repository.BalanceHistoryRepository;
import uz.pdp.springsecurity.repository.BalanceRepository;
import uz.pdp.springsecurity.repository.PayMethodRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository repository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final PayMethodRepository payMethodRepository;

    public ApiResponse edit(UUID branchId, Double summa, Boolean isPlus, UUID payMethodId) {
        Optional<Balance> optionalBalance = repository.findByPaymentMethod_IdAndBranchId(payMethodId, branchId);

        if (optionalBalance.isPresent()) {
            Balance balance = optionalBalance.get();
            if (summa > 0) {
                BalanceHistory newBalanceHistory = new BalanceHistory();
                newBalanceHistory.setBalance(balance);
                newBalanceHistory.setAccountSumma(balance.getAccountSumma());

                double totalSumma;
                if (Boolean.TRUE.equals( isPlus)) {
                    totalSumma = balance.getAccountSumma() + summa;
                } else {
                    totalSumma = balance.getAccountSumma() - summa;
                }
                balance.setAccountSumma(totalSumma);
                newBalanceHistory.setTotalSumma(totalSumma);
                newBalanceHistory.setPlus(isPlus);
                newBalanceHistory.setSumma(summa);

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
            balanceDtoList.add(balanceDto);
        }

        balanceDtoList.sort(Comparator.comparing(BalanceDto::getPaymentMethodId));

        return new ApiResponse("found", true, balanceDtoList);
    }

    public ApiResponse edit(UUID branchId, boolean isPlus, List<PaymentDto> paymentDtoList) {
        for (PaymentDto paymentMethod : paymentDtoList) {
            Optional<Balance> optionalBalance = repository.findByPaymentMethod_IdAndBranchId(paymentMethod.getPaymentMethodId(), branchId);
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
                    newBalanceHistory.setSumma((paymentMethod.getIsDollar() != null && paymentMethod.getIsDollar() ? paymentMethod.getPaidSumDollar() : paymentMethod.getPaidSum()));

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
        List<PaymentMethod> allPaymentMethod = payMethodRepository.findAllByBusiness_Id(businessId);

        List<BalanceGetDto> balanceGetDtoList = new ArrayList<>();
        for (PaymentMethod paymentMethod : allPaymentMethod) {
            List<Balance> all = repository.findAllByPaymentMethodId(paymentMethod.getId());
            BalanceGetDto balanceGetDto = new BalanceGetDto();
            double totalSumma = 0;
            for (Balance balance : all) {
                totalSumma = totalSumma + balance.getAccountSumma();
                balanceGetDto.setPaymentMethodId(balance.getPaymentMethod().getId());
                balanceGetDto.setPayMethodName(balance.getPaymentMethod().getType());
                balanceGetDto.setBalanceSumma(totalSumma);
            }
            balanceGetDtoList.add(balanceGetDto);
        }

        return new ApiResponse("all", true, balanceGetDtoList);
    }
}
