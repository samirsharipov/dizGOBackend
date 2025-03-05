package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeDto {
    private Timestamp arrivalTime;
    private Timestamp leaveTime;
    private double minute;
    private boolean active;
}
