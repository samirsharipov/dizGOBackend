package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormLidHistoryDto {
    private String name;
    private double totalSumma;
    private long totalLid;
    private double average;
    private UUID businessId;
    private boolean active;
}
