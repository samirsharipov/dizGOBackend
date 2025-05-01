package uz.dizgo.erp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uz.dizgo.erp.entity.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DiffUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * oldObj va newObj ni solishtiradi va faqat o‘zgargan maydonlarni ajratib beradi.
     *
     * @param oldObj eski obyekt (masalan, DB'dan olingan Product)
     * @param newObj yangi obyekt (yangilangan Product)
     * @return Map bilan "old_data" va "new_data" JSON sifatida foydalanishga tayyor
     */
    public static Map<String, Map<String, Object>> getDiff(Object oldObj, Object newObj) {
        Map<String, Object> oldMap = objectMapper.convertValue(oldObj, new TypeReference<>() {});
        Map<String, Object> newMap = objectMapper.convertValue(newObj, new TypeReference<>() {});

        Map<String, Object> oldDiff = new HashMap<>();
        Map<String, Object> newDiff = new HashMap<>();

        for (String key : newMap.keySet()) {
            Object oldVal = oldMap.get(key);
            Object newVal = newMap.get(key);

            if (!(oldVal == null && newVal == null) && !Objects.equals(oldVal, newVal)) {
                oldDiff.put(key, oldVal);
                newDiff.put(key, newVal);
            }
        }

        return Map.of(
                "old_data", oldDiff,
                "new_data", newDiff
        );
    }

    public static Map<String, Object> toFlatMap(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        return productMap;
    }
}
