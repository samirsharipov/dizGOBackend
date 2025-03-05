package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dizgo.erp.enums.HistoryName;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryGetDto {
    private UUID id;
    private Timestamp createdAt;
    private HistoryName name;
    private String description;
    private String firstName;
    private String lastName;
    private String branchName;
}
