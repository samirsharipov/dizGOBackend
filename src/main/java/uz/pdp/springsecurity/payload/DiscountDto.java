package uz.pdp.springsecurity.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountDto {
    private String name; // Chegirma nomi
    private String description; // Ixtiyoriy tavsif
    private String type; // Chegirma turi (foiz yoki summa)
    private double value; // Chegirma miqdori

    private Timestamp startDate; // Boshlanish sanasi
    private Timestamp endDate; // Tugash sanasi

    private List<UUID> productIds; // Bog'langan mahsulotlar
    private List<UUID> branchIds; // Bog'langan filliallar
}
