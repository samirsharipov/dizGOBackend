package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationGetByIdDto {
    private String name;
    private String message;
    private String type;
    private UUID objectId;
    private UUID attachmentId;
    private UUID branchId;
}
