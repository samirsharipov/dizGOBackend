package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDto {
    private UUID id;
    private String name;
    private String description;
    private Double percentage;
    private Double totalSum;
    private Date startDate;
    private Date endDate;
    private Double dailyAmount;
    private Date lastUpdateDate;
    private UUID branchId;
}
