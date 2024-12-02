package uz.pdp.springsecurity.service;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsSendService {
    @Value("${sms.api.url}")
    private String smsApiUrl;

    @Value("${sms.api.username}")
    private String username;

    @Value("${sms.api.password}")
    private String password;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sendSms(String recipient, String message) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(smsApiUrl + "/send");

            // JSON ma'lumotlarini tayyorlash
            Map<String, Object> smsData = new HashMap<>();
            smsData.put("messages", new Object[]{
                    Map.of(
                            "recipient", recipient,
                            "message-id", "unique-id-12345",
                            "content", Map.of("text", message)
                    )
            });

            // HTTP so'rovni sozlash
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(smsData));
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Basic " +
                    java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));

            try (CloseableHttpResponse response = client.execute(post)) {
                if (response.getCode() == 200) {
                    return "SMS successfully sent!";
                } else {
                    return "Failed to send SMS. Status code: " + response.getCode();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while sending SMS.";
        }
    }
}
