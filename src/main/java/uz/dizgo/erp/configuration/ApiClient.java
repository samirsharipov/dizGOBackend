package uz.dizgo.erp.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ApiClient {

    @Value("${sms.api.url}")
    private String baseUrl;

    @Value("${sms.api.username}")
    private String username;

    @Value("${sms.api.password}")
    private String password;

    private final RestTemplate restTemplate;

    public <T> T sendRequest(String endpoint, HttpMethod method, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Basic autentifikatsiya: login va parolni Base64 formatida kodlash
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        try {
            // So'rovni yuborish va javobni olish
            ResponseEntity<T> response = restTemplate.exchange(baseUrl + endpoint, method, entity, responseType);

            // Agar javob muvaffaqiyatli bo'lsa, body ni qaytarish
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("So'rov bajarilmadi. Status kodi: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Agar xatolik yuz bersa, xatolikni aniqlash va qaytarish
            throw new RuntimeException("Xatolik yuz berdi: " + e.getMessage(), e);
        }
    }
}