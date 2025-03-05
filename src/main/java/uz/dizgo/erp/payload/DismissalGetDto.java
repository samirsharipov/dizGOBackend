package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DismissalGetDto {
    private UUID id;
    private Timestamp createAt;
    private UUID dismissalDescriptionId;
    private UUID userid;
    private String firstname;
    private String lastname;
    private String description;
    private String comment;
    private boolean mandatory;
}
