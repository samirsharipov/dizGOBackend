package uz.dizgo.erp.service.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uz.dizgo.erp.entity.PaymentTransaction;
import uz.dizgo.erp.entity.UserCard;
import uz.dizgo.erp.payload.TransactionalDto;
import uz.dizgo.erp.repository.PaymentTransactionRepository;
import uz.dizgo.erp.repository.UserCardRepository;
import uz.dizgo.erp.service.MessageService;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PlumPaymentService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final MessageService messageService;
    private final UserCardRepository userCardRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public PlumPaymentService(@Value("${plum.api.base-url}") String baseUrl,
                              @Value("${plum.api.username}") String username,
                              @Value("${plum.api.password}") String password,
                              RestTemplateBuilder restTemplateBuilder,
                              MessageService messageService,
                              UserCardRepository userCardRepository, PaymentTransactionRepository paymentTransactionRepository) {
        this.baseUrl = baseUrl;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        this.restTemplate = restTemplateBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .build();
        this.messageService = messageService;
        this.userCardRepository = userCardRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    private ResponseEntity<?> handleRequestWithHeaders(String url, HttpMethod method, Map<String, Object> request, boolean isSaveCard) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, method, entity, Map.class);

            if (isSaveCard) {
                if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
                    saveUserCard(response.getBody());
                }
            }
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageService.getMessage("error.message") + e.getMessage());
        }
    }

    private void saveUserCard(Map<String, Object> responseBody) {
        Map<String, Object> cardData = (Map<String, Object>) responseBody.get("result");
        if (cardData != null) {
            Map<String, Object> resultMap = (Map<String, Object>) cardData;
            Object cardObj = resultMap.get("card");
            if (cardObj instanceof Map) {
                Map<String, Object> cardMap = (Map<String, Object>) cardObj;
                Object userId = cardMap.get("userId");
                UUID uuid = UUID.fromString(userId.toString());
                String cardNumber = cardMap.get("number").toString();


                UserCard userCard = new UserCard();
                Optional<UserCard> optionalUserCard = userCardRepository.findByUserIdAndCardNumber(uuid, cardNumber);
                if (optionalUserCard.isPresent()) {
                    userCard = optionalUserCard.get();
                } else {
                    userCard.setUserId(uuid);
                    userCard.setCardNumber(cardNumber);
                }

                userCard.setCardId(Long.parseLong(cardMap.get("cardId").toString()));
                userCard.setExpireDate(cardMap.get("expireDate").toString());
                userCard.setOwnerName(cardMap.get("owner").toString());
                userCard.setBalance(new BigDecimal(cardMap.get("balance").toString()));
                userCard.setStatus(cardMap.get("status").toString());

                userCardRepository.save(userCard);
            }
        }
    }

    // ✅ 1. Kartaga tegishli egani haqida ma'lumot olish
    public ResponseEntity<?> getCardOwnerInfoByPan(String cardNumber) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/getCardOwnerInfoByPan", HttpMethod.POST, Map.of("cardNumber", cardNumber), false);
    }

    // ✅ 2. Foydalanuvchiga tegishli barcha kartalarni olish
    public ResponseEntity<?> getAllUserCards(String userId) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/getAllUserCards?userId=" + userId, HttpMethod.GET, null, false);
    }

    // ✅ 3. Kartani o‘chirish
    public ResponseEntity<?> deleteUserCard(Long userCardId) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/deleteUserCard?userCardId=" + userCardId, HttpMethod.DELETE, null, false);
    }

    // ✅ 4. Kartani yaratish va bazaga saqlash
    public ResponseEntity<?> createUserCard(String userId, String cardNumber, String expireDate, String userPhone, String pinfl) {
        String url = baseUrl + "/UserCard/createUserCard";
        Map<String, Object> request = new HashMap<>(Map.of(
                "userId", userId,
                "cardNumber", cardNumber,
                "expireDate", expireDate,
                "userPhone", userPhone
        ));
        if (pinfl != null && !pinfl.isEmpty()) {
            request.put("pinfl", pinfl);
        }
        return handleRequestWithHeaders(url, HttpMethod.POST, request, false);
    }

    // ✅ 5. Kartani tasdiqlash (OTP orqali)
    public ResponseEntity<?> confirmUserCardCreate(Long session, String otp, Boolean isTrusted, String cardName) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/confirmUserCardCreate", HttpMethod.POST, Map.of(
                "session", session,
                "otp", otp,
                "isTrusted", isTrusted != null ? isTrusted ? 1 : 0 : 0,
                "cardName", cardName != null ? cardName : ""
        ), true);
    }

    // ✅ 6. OTP qayta yuborish
    public ResponseEntity<?> resendOtp(Long session) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/resendOtp?session=" + session, HttpMethod.GET, null, false);
    }

    // ✅ 7. Oddiy to‘lov qilish
    public ResponseEntity<?> createPayment(String userId, Long cardId, BigDecimal amount, String extraId, TransactionalDto transactionalDto) {

        ResponseEntity<?> responseEntity = handleRequestWithHeaders(baseUrl + "/Payment/payment", HttpMethod.POST, Map.of(
                "userId", userId,
                "cardId", cardId,
                "amount", amount,
                "extraId", extraId
        ), false);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK) && responseEntity.getBody() != null && transactionalDto != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON ni Map ga o‘girish
            Map<String, Object> responseMap = objectMapper.convertValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
            });

            // "result" obyektini olish
            Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("result");

            // transactionId ni olish
            long transactionId = ((Number) resultMap.get("transactionId")).longValue();

            PaymentTransaction paymentTransaction = new PaymentTransaction(transactionalDto.getCustomerId(),
                    transactionalDto.getBranchId(),
                    transactionalDto.getTotalAmount(),
                    transactionalDto.getPaidAmount(),
                    transactionalDto.getDiscountAmount(),
                    transactionId);
            paymentTransaction.setActive(false);
            paymentTransactionRepository.save(paymentTransaction);
        }

        return responseEntity;
    }

    // ✅ 8. Ro‘yxatdan o‘tmagan foydalanuvchi uchun to‘lov
    public ResponseEntity<?> paymentWithoutRegistration(String cardNumber, String expireDate, BigDecimal amount) {
        return handleRequestWithHeaders(baseUrl + "/Payment/paymentWithoutRegistration", HttpMethod.POST, Map.of(
                "cardNumber", cardNumber,
                "expireDate", expireDate,
                "amount", amount
        ), false);
    }

    // ✅ 9. To‘lovni tasdiqlash (OTP orqali)
    public ResponseEntity<?> confirmPayment(Long session, String otp) {
        ResponseEntity<?> responseEntity = handleRequestWithHeaders(baseUrl + "/Payment/confirmPayment", HttpMethod.POST, Map.of(
                "session", session,
                "otp", otp
        ), false);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            ObjectMapper objectMapper = new ObjectMapper();

            // JSON ni Map ga o‘girish
            Map<String, Object> responseMap = objectMapper.convertValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
            });

            // "result" obyektini olish
            Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("result");

            // transactionId ni olish
            long transactionId = ((Number) resultMap.get("transactionId")).longValue();

            Optional<PaymentTransaction> optional =
                    paymentTransactionRepository.findByTransactionIdAndActiveFalse(transactionId);
            if (optional.isPresent()) {
                PaymentTransaction paymentTransaction = optional.get();
                paymentTransaction.setActive(true);
                paymentTransactionRepository.save(paymentTransaction);
            }

        }
        return responseEntity;
    }

    // ✅ 10. To‘lov tranzaktsiyalarini olish
    public ResponseEntity<?> getTransactions(String userId) {
        return handleRequestWithHeaders(baseUrl + "/Payment/getTransactions?userId=" + userId, HttpMethod.GET, null, false);
    }

    // ✅ 11. To‘lovni qaytarish
    public ResponseEntity<?> paymentReverse(Long transactionId) {
        return handleRequestWithHeaders(baseUrl + "/Payment/paymentReverse", HttpMethod.POST, Map.of("transactionId", transactionId), false);
    }
}