package uz.pdp.springsecurity.payload.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStat {
    private int totalLateDays;           // Kechikib kelgan kunlar soni
    private long totalLateMinutes;       // Kechikishning umumiy vaqti (daqiqalarda)
    private int totalEarlyLeaveDays;     // Erta ketgan kunlar soni
    private long totalEarlyLeaveMinutes; // Erta ketishning umumiy vaqti (daqiqalarda)
    private double totalOvertimeHours;   //
}
