package uz.pdp.springsecurity.payload;

import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class DiscountEditDto {
    private List<UUID> newProductIds;
    private List<UUID> oldProductIds;
    private double value;
    private Timestamp startDate;
    private Timestamp endDate;
    private Time startHour;
    private Time endHour;
    private Set<Integer> weekDays;
}
