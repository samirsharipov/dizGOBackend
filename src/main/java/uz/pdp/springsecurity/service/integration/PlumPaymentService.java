package uz.pdp.springsecurity.service.integration;


import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Map;

@Service
public class PlumPaymentService {
//
//    @Value("${plum.api.base-url}")
//    private String baseUrl;
//
//    @Value("${plum.api.username}")
//    private String username;
//
//    @Value("${plum.api.password}")
//    private String password;
//
//    private final RestTemplate restTemplate;
//
//    public PlumPaymentService(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplate = restTemplateBuilder
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder()
//                        .encodeToString((username + ":" + password).getBytes()))
//                .build();
//    }
//
//    public String getAllUserCards(String userId) {
//        String url = baseUrl + "/UserCard/getAllUserCards?userId=" + userId;
//        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        return response.getBody();
//    }
//
//    public String createPayment(String userId, Long cardId, BigDecimal amount, String extraId) {
//        String url = baseUrl + "/Payment/payment";
//        Map<String, Object> request = Map.of(
//                "userId", userId,
//                "cardId", cardId,
//                "amount", amount,
//                "extraId", extraId
//        );
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//        return response.getBody();
//    }
}
