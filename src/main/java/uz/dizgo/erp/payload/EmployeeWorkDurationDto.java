package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWorkDurationDto {
    private UUID id;
    private UUID employeeId;
    private String fullName;
    private Long totalWorkDuration; // Ishlagan umumiy vaqt (sekundlarda)
}