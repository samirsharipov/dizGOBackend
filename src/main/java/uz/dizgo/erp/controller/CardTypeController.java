package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.dizgo.erp.helpers.ResponseEntityHelper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.security.CardTypeService;

@RestController
@RequestMapping("/api/cardType")
@RequiredArgsConstructor
public class CardTypeController {

    private final CardTypeService cardTypeService;
    private final ResponseEntityHelper responseEntityHelper;

    @GetMapping("/check")
    public ResponseEntity<ApiResponse> check(@RequestParam String cardNumber) {
        return responseEntityHelper.buildResponse(cardTypeService.getCardType(cardNumber));
    }
}
