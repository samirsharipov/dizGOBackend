package uz.pdp.springsecurity.service.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uz.pdp.springsecurity.entity.UserCard;
import uz.pdp.springsecurity.repository.UserCardRepository;
import uz.pdp.springsecurity.service.MessageService;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PlumPaymentService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final MessageService messageService;
    private final UserCardRepository userCardRepository;

    public PlumPaymentService(@Value("${plum.api.base-url}") String baseUrl,
                              @Value("${plum.api.username}") String username,
                              @Value("${plum.api.password}") String password,
                              RestTemplateBuilder restTemplateBuilder,
                              MessageService messageService,
                              UserCardRepository userCardRepository) {
        this.baseUrl = baseUrl;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        this.restTemplate = restTemplateBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .build();
        this.messageService = messageService;
        this.userCardRepository = userCardRepository;
    }

    private ResponseEntity<?> handleRequestWithHeaders(String url, HttpMethod method, Map<String, Object> request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, method, entity, Map.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageService.getMessage("error.message") + e.getMessage());
        }
    }

    private void saveUserCard(String userId, Map<String, Object> responseBody) {
        Map<String, Object> cardData = (Map<String, Object>) responseBody.get("result");
        if (cardData != null) {
            UserCard userCard = new UserCard();
            userCard.setUserId(UUID.fromString(userId));
            userCard.setCardId(Long.parseLong(cardData.get("cardId").toString()));
            userCard.setCardNumber(cardData.get("number").toString());
            userCard.setExpireDate(cardData.get("expireDate").toString());
            userCard.setOwnerName(cardData.get("owner").toString());
            userCard.setBalance(new BigDecimal(cardData.get("balance").toString()));
            userCard.setStatus(cardData.get("status").toString());

            userCardRepository.save(userCard);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());
        return headers;
    }

    // ✅ 1. Kartaga tegishli egani haqida ma'lumot olish
    public ResponseEntity<?> getCardOwnerInfoByPan(String cardNumber) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/getCardOwnerInfoByPan", HttpMethod.POST, Map.of("cardNumber", cardNumber));
    }

    // ✅ 2. Foydalanuvchiga tegishli barcha kartalarni olish
    public ResponseEntity<?> getAllUserCards(String userId) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/getAllUserCards?userId=" + userId, HttpMethod.GET, null);
    }

    // ✅ 3. Kartani o‘chirish
    public ResponseEntity<?> deleteUserCard(Long cardId) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/deleteUserCard", HttpMethod.POST, Map.of("cardId", cardId));
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

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, createHeaders()), Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            saveUserCard(userId, response.getBody());
        }
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // ✅ 5. Kartani tasdiqlash (OTP orqali)
    public ResponseEntity<?> confirmUserCardCreate(Long session, String otp, boolean isTrusted, String cardName) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/confirmUserCardCreate", HttpMethod.POST, Map.of(
                "session", session,
                "otp", otp,
                "isTrusted", isTrusted ? 1 : 0,
                "cardName", cardName
        ));
    }

    // ✅ 6. OTP qayta yuborish
    public ResponseEntity<?> resendOtp(Long session) {
        return handleRequestWithHeaders(baseUrl + "/UserCard/resendOtp?session=" + session, HttpMethod.GET, null);
    }

    // ✅ 7. Oddiy to‘lov qilish
    public ResponseEntity<?> createPayment(String userId, Long cardId, BigDecimal amount, String extraId) {
        return handleRequestWithHeaders(baseUrl + "/Payment/payment", HttpMethod.POST, Map.of(
                "userId", userId,
                "cardId", cardId,
                "amount", amount,
                "extraId", extraId
        ));
    }

    // ✅ 8. Ro‘yxatdan o‘tmagan foydalanuvchi uchun to‘lov
    public ResponseEntity<?> paymentWithoutRegistration(String cardNumber, String expireDate, BigDecimal amount) {
        return handleRequestWithHeaders(baseUrl + "/Payment/paymentWithoutRegistration", HttpMethod.POST, Map.of(
                "cardNumber", cardNumber,
                "expireDate", expireDate,
                "amount", amount
        ));
    }

    // ✅ 9. To‘lovni tasdiqlash (OTP orqali)
    public ResponseEntity<?> confirmPayment(Long session, String otp) {
        return handleRequestWithHeaders(baseUrl + "/Payment/confirmPayment", HttpMethod.POST, Map.of(
                "session", session,
                "otp", otp
        ));
    }

    // ✅ 10. To‘lov tranzaktsiyalarini olish
    public ResponseEntity<?> getTransactions(String userId) {
        return handleRequestWithHeaders(baseUrl + "/Payment/getTransactions?userId=" + userId, HttpMethod.GET, null);
    }

    // ✅ 11. To‘lovni qaytarish
    public ResponseEntity<?> paymentReverse(Long transactionId) {
        return handleRequestWithHeaders(baseUrl + "/Payment/paymentReverse", HttpMethod.POST, Map.of("transactionId", transactionId));
    }
}