package uz.pdp.springsecurity.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import uz.pdp.springsecurity.configuration.ApiClient;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SmsSendService {

    private final ApiClient apiClient;

    public SmsSendService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * SMS yuborish
     *
     * @param recipient Qabul qiluvchining telefon raqami
     * @param content   Xabar matni
     * @return SMS yuborilganligi haqida xabar
     */
    public String sendSms(String recipient, String content) {
        String messageId = generateMessageId();
        String endpoint = "/send"; // API endpoint

        // JSON ma'lumotlarini tayyorlash
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of(
                                "recipient", recipient,
                                "message-id", messageId,
                                "sms", Map.of(
                                        "originator", "3700", // Yuboruvchining nomi yoki raqami
                                        "content", Map.of("text", content) // Xabar mazmuni
                                )
                        )
                )
        );

        // API'ga so'rov yuborish
        return apiClient.sendRequest(endpoint, HttpMethod.POST, requestBody, String.class);
    }

    /**
     * SMS holatini tekshirish
     *
     * @param messageId SMS yuborilgan xabar ID'si
     * @return Xabar holati
     */
    public String checkSmsStatus(String messageId) {
        String endpoint = "/status"; // API endpoint for status check

        // Statusni tekshirish uchun JSON ma'lumotlari
        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of(
                                "message-id", messageId,
                                "channel", "sms" // SMS kanalini ko'rsatish
                        )
                )
        );

        // API'ga status so'rovini yuborish
        return apiClient.sendRequest(endpoint, HttpMethod.POST, requestBody, String.class);
    }


    public String generateMessageId() {
        // Hozirgi vaqtni olish (timestamp)
        long timestamp = System.currentTimeMillis();

        // Tasodifiy raqam yaratish
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // 0-999 gacha tasodifiy raqam

        // timestamp va random raqamni birlashtirish
        return "msg-" + timestamp + "-" + randomNumber;
    }

    public void sendVerificationCode(String phoneNumber, String code) {
        String message = "Kod dlya vhoda v prilozheniyu Business Navigator: " + code + ". Ne soobshayte danniy kod nikomu!!!";
        sendSms(phoneNumber, message);
    }
}