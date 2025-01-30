package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.enums.DiscountType;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountGetDto {
    private UUID id; // Chegirma ID
    private List<ProductInfo> products; // Bog‘langan mahsulotlar ID va nomlari
    private double discountValue; // Amaldagi chegirma miqdori
    private boolean active; // Chegirma statusi (Faol / Nofaol)
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;

    private Time startHour;
    private Time endHour;
    private Set<Integer> weekDays;
    private DiscountType discountType;
    private List<UUID> brancheIdList;
    private List<String> branchNameList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private UUID id; // Mahsulot ID si
        private String name; // Mahsulot nomi
        private double price;
    }
}