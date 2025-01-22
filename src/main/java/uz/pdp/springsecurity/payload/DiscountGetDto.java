package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountGetDto {
    private UUID id; // Chegirma ID
    private List<ProductInfo> products; // Bog‘langan mahsulotlar ID va nomlari
    private double discountValue; // Amaldagi chegirma miqdori
    private String duration; // Chegirma muddati (boshlanish - tugash)
    private boolean active; // Chegirma statusi (Faol / Nofaol)

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private UUID id; // Mahsulot ID si
        private String name; // Mahsulot nomi
    }
}