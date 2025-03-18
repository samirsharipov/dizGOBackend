package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.PocketMoneyTransactionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/pocket-money-transaction")
@RequiredArgsConstructor
public class PocketMoneyTransactionController {

    private final ResponseEntityHelper responseEntityHelper;
    private final PocketMoneyTransactionService pocketMoneyTransactionService;

    @GetMapping("/get-all-by-cashier-id/{cashierUserId}")
    public ResponseEntity<ApiResponse>getAllByCashierId(@PathVariable UUID cashierUserId) {
        return responseEntityHelper.buildResponse(pocketMoneyTransactionService.getAllByUserId(cashierUserId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse>getById(@PathVariable UUID id) {
        return responseEntityHelper.buildResponse(pocketMoneyTransactionService.getById(id));
    }
}
