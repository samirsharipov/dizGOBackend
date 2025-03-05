package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.Status;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Message extends AbsEntity {
    private UUID senderId;
    private UUID receiverId;
    private String receiverName;
    private String senderName;
    private String message;
    @ManyToOne
    private Attachment attachment;
    private String date;
    private Status status;
}
