package uz.dizgo.erp.service.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uz.dizgo.erp.payload.integtation.ZKongAddProductDto;
import uz.dizgo.erp.payload.integtation.ZKongSession;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class ZKongService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String username;
    private final String password;

    private ZKongSession session;

    public ZKongService(@Value("${zkong.api.base-url}") String baseUrl,
                        @Value("${zkong.api.username}") String username,
                        @Value("${zkong.api.password}") String password,
                        RestTemplateBuilder builder) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.restTemplate = builder.build();
    }


    @FunctionalInterface
    public interface ZkongApiCall {
        boolean execute() throws Exception;
    }

    private boolean withTokenRetry(ZkongApiCall action) {
        try {
            return action.execute();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && e.getResponseBodyAsString().contains("10006")) {
                System.out.println("🔁 Token eskirgan, yangilanmoqda...");
                this.session = null; // tokenni reset qilamiz
                try {
                    return action.execute(); // qayta so‘rov
                } catch (Exception retryEx) {
                    retryEx.printStackTrace();
                    return false;
                }
            } else {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getToken() {
        if (session == null || Instant.now().isAfter(session.getExpiry())) {
            this.session = loginAndGetSession();
        }
        return session.getToken();
    }

    public Long getMerchantId() {
        return getSession().getMerchantId();
    }

    public Long getAgencyId() {
        return getSession().getAgencyId();
    }

    private ZKongSession getSession() {
        if (session == null || Instant.now().isAfter(session.getExpiry())) {
            this.session = loginAndGetSession();
        }
        return session;
    }

    private ZKongSession loginAndGetSession() {
        String publicKey = getPublicKey();
        String encryptedPassword = encryptPasswordWithRSA(password, publicKey);

        Map<String, Object> body = new HashMap<>();
        body.put("account", username);
        body.put("password", encryptedPassword);
        body.put("loginType", 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String url = baseUrl + "/zk/user/login";
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody().get("success").asBoolean()) {
            JsonNode data = response.getBody().get("data");
            String token = data.get("token").asText();
            JsonNode user = data.get("currentUser");
            Long merchantId = user.get("merchantId").asLong();
            Long agencyId = user.get("agencyId").asLong();

            return new ZKongSession(token, Instant.now().plus(Duration.ofHours(1)), merchantId, agencyId);
        }

        throw new RuntimeException("Login xatolik: " + response.getBody().get("message").asText());
    }

    private String getPublicKey() {
        String url = baseUrl + "/zk/user/getErpPublicKey";
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody().get("success").asBoolean()) {
            return response.getBody().get("data").asText();
        }
        throw new RuntimeException("Public key olishda xatolik");
    }

    private String encryptPasswordWithRSA(String password, String publicKeyPem) {
        try {
            byte[] decoded = Base64.getDecoder().decode(publicKeyPem);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("RSA encrypt qilishda xatolik", e);
        }
    }


    public boolean updatePrice(String barcode, Double price, Long storeId) {
        try {
            String token = getToken();
            Long merchantId = getMerchantId();
            Long agencyId = getAgencyId();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            Map<String, Object> product = new HashMap<>();
            product.put("barCode", barcode);
            product.put("attrCategory", "default");
            product.put("attrName", "default");
            product.put("price", price);
            product.put("itemTitle", "Auto Updated"); // yoki mavjud nom

            List<Map<String, Object>> itemList = new ArrayList<>();
            itemList.add(product);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("storeId", storeId);
            requestBody.put("merchantId", merchantId);
            requestBody.put("agencyId", agencyId);
            requestBody.put("itemList", itemList);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String url = baseUrl + "/zk/item/batchImportItem";

            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            return response.getStatusCode().is2xxSuccessful() && response.getBody().get("success").asBoolean();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bindProductToDevice(ZKongAddProductDto addProductDto) {
        return withTokenRetry(() -> {
            String token = getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            Map<String, Object> body = new HashMap<>();
            body.put("itemBarCode", addProductDto.getItemBarcode());
            body.put("priceTagCode", addProductDto.getEslBarcode());
            body.put("storeId", addProductDto.getStoreId());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            String url = baseUrl + "/zk/bind/bindItemPriceTag/1";

            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
            return response.getStatusCode().is2xxSuccessful() && response.getBody().get("success").asBoolean();
        });
    }


}
