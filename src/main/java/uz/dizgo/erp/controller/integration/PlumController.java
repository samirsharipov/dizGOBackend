package uz.dizgo.erp.controller.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.dizgo.erp.payload.TransactionalDto;
import uz.dizgo.erp.service.integration.PlumPaymentService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plum")
public class PlumController {

    private final PlumPaymentService plumPaymentService;

    // ✅ 1. Kartaga tegishli egani haqida ma'lumot olish
    @PostMapping("/card-owner-info")
    public ResponseEntity<?> getCardOwnerInfoByPan(@RequestParam String cardNumber) {
        return plumPaymentService.getCardOwnerInfoByPan(cardNumber);
    }

    // ✅ 2. Foydalanuvchiga tegishli barcha kartalarni olish
    @GetMapping("/user-cards")
    public ResponseEntity<?> getAllUserCards(@RequestParam String userId) {
        return plumPaymentService.getAllUserCards(userId);
    }

    // ✅ 3. Kartani o‘chirish
    @DeleteMapping("/delete-card")
    public ResponseEntity<?> deleteUserCard(@RequestParam Long userCardId) {
        return plumPaymentService.deleteUserCard(userCardId);
    }

    // ✅ 4. Kartani yaratish (OTP yuboriladi)
    @PostMapping("/create-card")
    public ResponseEntity<?> createUserCard(@RequestParam String userId,
                                            @RequestParam String cardNumber,
                                            @RequestParam String expireDate,
                                            @RequestParam String userPhone,
                                            @RequestParam(required = false) String pinfl) {
        return plumPaymentService.createUserCard(userId, cardNumber, expireDate, userPhone, pinfl);
    }

    // ✅ 5. Kartani tasdiqlash (OTP orqali)
    @PostMapping("/confirm-card")
    public ResponseEntity<?> confirmUserCard(@RequestParam Long session,
                                             @RequestParam String otp,
                                             @RequestParam(required = false) Boolean isTrusted,
                                             @RequestParam(required = false) String cardName) {
        return plumPaymentService.confirmUserCardCreate(session, otp, isTrusted, cardName);
    }

    // ✅ 6. OTP qayta yuborish
    @GetMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam Long session) {
        return plumPaymentService.resendOtp(session);
    }

    // ✅ 7. Oddiy to‘lov qilish
    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestParam String userId,
                                           @RequestParam Long cardId,
                                           @RequestParam BigDecimal amount,
                                           @RequestParam String extraId,
                                           @RequestParam(required = false) TransactionalDto transactionalDto) {
        return plumPaymentService.createPayment(userId, cardId, amount, extraId, transactionalDto);
    }

    // ✅ 8. Ro‘yxatdan o‘tmagan foydalanuvchi uchun to‘lov
    @PostMapping("/payment-without-registration")
    public ResponseEntity<?> paymentWithoutRegistration(@RequestParam String cardNumber,
                                                        @RequestParam String expireDate,
                                                        @RequestParam BigDecimal amount) {
        return plumPaymentService.paymentWithoutRegistration(cardNumber, expireDate, amount);
    }

    // ✅ 9. To‘lovni tasdiqlash (OTP orqali)
    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestParam Long session,
                                            @RequestParam String otp) {
        return plumPaymentService.confirmPayment(session, otp);
    }

    // ✅ 10. To‘lov tranzaktsiyalarini olish
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam String userId) {
        return plumPaymentService.getTransactions(userId);
    }

    // ✅ 11. To‘lovni qaytarish
    @PostMapping("/payment-reverse")
    public ResponseEntity<?> paymentReverse(@RequestParam Long transactionId) {
        return plumPaymentService.paymentReverse(transactionId);
    }
}