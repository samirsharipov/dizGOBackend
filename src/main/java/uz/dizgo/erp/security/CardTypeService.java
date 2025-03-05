package uz.dizgo.erp.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import uz.dizgo.erp.payload.ApiResponse;

@Service
public class CardTypeService {
    private final Map<String, String> cache = new ConcurrentHashMap<>(); // ✅ Thread-safe cache
    private final RestTemplate restTemplate;
    private static final int BIN_LENGTH = 6;
    private static final int MAX_CACHE_SIZE = 1000; // ✅ Kesh hajmi cheklovi

    public CardTypeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            value = HttpClientErrorException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2))// ✅ 2s, 4s, 8s kutish)
    public ApiResponse getCardType(String cardNumber) {
        // Input validation
        if (!isValidCardNumber(cardNumber)) {
            return new ApiResponse("Noto‘g‘ri karta raqami! Kamida 6 raqam bo‘lishi kerak.", false);
        }

        String bin = extractBin(cardNumber);

        // Cache tekshiruvi
        String cachedType = cache.get(bin);
        if (cachedType != null) {
            return new ApiResponse("Karta topildi!", true, cachedType);
        }

        // API so‘rovi
        try {
            String cardType = fetchCardTypeFromApi(bin);
            putToCache(bin, cardType);
            return new ApiResponse("Karta topildi!", true, cardType);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw e; // Retry mexanizmi ishlasin
            }
            return new ApiResponse("Bu karta turi ma’lumotlar bazasida yo‘q!", false);
        } catch (Exception e) {
            return new ApiResponse("Serverda xatolik yuz berdi: " + e.getMessage(), false);
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.length() >= BIN_LENGTH;
    }

    private String extractBin(String cardNumber) {
        return cardNumber.substring(0, BIN_LENGTH);
    }

    private String fetchCardTypeFromApi(String bin) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100); // ✅ Kichikroq delay
        String url = "https://lookup.binlist.net/" + bin;
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("scheme")) {
                return response.get("scheme").toString();
            }
        }catch (HttpClientErrorException e) {
            return e.getResponseBodyAsString();
        }

        throw new IllegalStateException("Karta turi topilmadi");
    }

    private void putToCache(String bin, String cardType) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            cache.clear(); // Yoki LRU (Least Recently Used) logikasini qo‘shish mumkin
        }
        cache.put(bin, cardType);
    }
}