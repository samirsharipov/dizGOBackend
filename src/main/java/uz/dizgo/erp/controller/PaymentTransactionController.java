package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.service.PaymentTransactionService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/plum-transaction")
@RequiredArgsConstructor
public class PaymentTransactionController {
    private final ResponseEntityHelper responseEntityHelper;
    private final PaymentTransactionService paymentTransactionService;

    @GetMapping("/get-by-branch-id/{branchId}")
    public ResponseEntity<ApiResponse> getAllBranchId(@PathVariable UUID branchId) {
        return responseEntityHelper.buildResponse(paymentTransactionService.getAllBranchId(branchId));
    }
}
