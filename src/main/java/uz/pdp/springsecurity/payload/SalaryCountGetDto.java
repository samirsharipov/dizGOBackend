package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCountGetDto {
    private UUID userId;

    private String firstName;

    private String lastName;

    private UUID id;

    private double count;

    private double salary;

    private Date date;

    private UUID agreementId;

    private String agreementName;

    private String description;
}
