package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidGetDto {
    private UUID id;
    private Map<String, String> values;
    private UUID lidStatusId;
    private String lidStatusName;
    private UUID businessId;
    private Timestamp timestamp;
    private String description;
    private String userName;
}
