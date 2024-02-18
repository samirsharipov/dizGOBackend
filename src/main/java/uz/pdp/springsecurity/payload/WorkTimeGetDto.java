package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkTimeGetDto {
    private UUID userID;
    private String firstName;
    private String lastName;
    private Timestamp arrivalTime;
    private boolean active;
}
