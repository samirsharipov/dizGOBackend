package uz.pdp.springsecurity.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class DiscountEditDto {
    private List<UUID> productIds;
    private double value;
    private Timestamp startTime;
    private Timestamp endTime;
}
