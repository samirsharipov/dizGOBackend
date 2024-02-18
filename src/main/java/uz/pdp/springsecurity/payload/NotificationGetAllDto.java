package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationGetAllDto {
    private UUID id;
    private String name;
    private boolean read;
    private Timestamp createdAt;
    private UUID objectId;
    private UUID branchId;
}
