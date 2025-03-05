package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dizgo.erp.enums.Status;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private UUID senderId;
    private UUID receiverId;
    private UUID attachmentId;
    private String receiverName;
    private String senderName;
    private String message;
    private String date;
    private Status status;
}
