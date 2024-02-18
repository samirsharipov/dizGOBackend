package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeDayDto {
    private UUID userId;
    private String firstName;
    private String lastName;
    List<TimeStampDto> timestampList;
    List<Timestamp> timestampListForLeave;
    String arrivalTime;
    private double hour;
}
