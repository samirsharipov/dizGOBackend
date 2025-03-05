package uz.dizgo.erp.payload;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class DiscountDto {
    private String name;
    private String description;
    private String type; // Enum sifatida qabul qilinadi
    private double value;
    
    @FutureOrPresent(message = "Sana bugungi kundan oldin bo'lishi mumkin emas!")
    private Timestamp startDate;
    private Timestamp endDate;
    private Time startHour;
    private Time endHour;
    private Set<Integer> weekDays; // Hafta kunlari

    private List<UUID> productIds;
    private List<UUID> branchIds;
}