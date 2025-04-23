package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.PaymentTransaction;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.PaymentTransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final MessageService messageService;

    public ApiResponse getAllBranchId(UUID branchId) {
        List<PaymentTransaction> all =
                paymentTransactionRepository.findByBranchId(branchId);
        if (all.isEmpty()) {
            return new ApiResponse(messageService.getMessage("not.found"), false);
        }
        return new ApiResponse(messageService.getMessage("success"), true, all);
    }
}
