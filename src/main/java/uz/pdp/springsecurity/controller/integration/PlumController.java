package uz.pdp.springsecurity.controller.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.springsecurity.service.integration.PlumPaymentService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plum")
public class PlumController {
    private final PlumPaymentService plumPaymentService;

//
//    @GetMapping("/cards/{userId}")
//    public ResponseEntity<String> getAllUserCards(@PathVariable String userId) {
//        return ResponseEntity.ok(plumPaymentService.getAllUserCards(userId));
//    }
//
//    @PostMapping("/payment")
//    public ResponseEntity<String> createPayment(@RequestParam String userId,
//                                                @RequestParam Long cardId,
//                                                @RequestParam BigDecimal amount,
//                                                @RequestParam String extraId) {
//        return ResponseEntity.ok(plumPaymentService.createPayment(userId, cardId, amount, extraId));
//    }
}
