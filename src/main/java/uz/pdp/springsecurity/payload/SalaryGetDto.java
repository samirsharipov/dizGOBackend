package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryGetDto {
    private UUID salaryId;

    private UUID userId;

    private String firstName;

    private String lastName;

    private double remain = 0;

    private double salary = 0;

    private double payedSum = 0;

    private Date startDate;

    private Date endDate;

    private boolean active;

    private String description;
    private double shouldPaySum;
}
