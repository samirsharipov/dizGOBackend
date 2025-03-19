package uz.dizgo.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.dizgo.erp.entity.PocketMoneyTransaction;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.repository.PocketMoneyTransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PocketMoneyTransactionService {
    private final PocketMoneyTransactionRepository repository;
    private final MessageService messageService;

    public ApiResponse getAllByUserId(UUID cashierUserId) {

        List<PocketMoneyTransaction> all =
                repository.findAllByCashierUserId(cashierUserId);

        if (all.isEmpty())
            return new ApiResponse(messageService.getMessage("not.found"), false);

        return new ApiResponse(messageService.getMessage("found"), true, all);
    }

    public ApiResponse getById(UUID id) {
        return repository.findById(id)
                .map(pocketMoneyTransaction ->
                        new ApiResponse(messageService.getMessage("found"), true, pocketMoneyTransaction))
                .orElse(new ApiResponse(messageService.getMessage("not.found"), false));
    }
}
