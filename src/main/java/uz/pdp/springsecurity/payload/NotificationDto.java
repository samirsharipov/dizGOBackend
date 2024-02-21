package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private String name;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    private String type;

    private String notificationKay;

    private UUID shablonId;

    private UUID objectId;

    private UUID businessOrBranchId;

    private UUID userFromId;

    private UUID attachmentId;
    private List<UUID> userToId;

    public NotificationDto(String name, String message, String type, String notificationKay, UUID businessOrBranchId,List<UUID> userToId) {
        this.name = name;
        this.message = message;
        this.type = type;
        this.notificationKay = notificationKay;
        this.businessOrBranchId = businessOrBranchId;
        this.userToId=userToId;
    }
}
