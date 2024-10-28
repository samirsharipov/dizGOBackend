package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.NotificationType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification extends AbsEntity {

    private String name;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    private UUID objectId;

    @ManyToOne
    private User userFrom;

    @ManyToOne
    private User userTo;

    @ManyToOne
    private Attachment attachment;

    private UUID branchId;

    private boolean delivery;

    private boolean read;
}
