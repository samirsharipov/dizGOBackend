package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceGetDto {
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private boolean arrived;
    private String branchName;
}
