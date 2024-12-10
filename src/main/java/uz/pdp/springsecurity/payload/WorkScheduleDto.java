package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkScheduleDto {
    private UUID id;

    private UUID userId;

    private LocalTime startTime; // kelish vaqti

    private LocalTime endTime; // ketish vaqti

    private LocalTime breakStart; // tanafus vaqti

    private LocalTime breakEnd; // tanafus tugash vaqti

    private String workDays; // Haftaning ishlaydigan kunlari (1-7 formatida)
}
