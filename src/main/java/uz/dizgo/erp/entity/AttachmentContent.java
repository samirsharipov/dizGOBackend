package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AttachmentContent extends AbsEntity {
    private byte[] mainContent;
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Attachment attachment;

    public AttachmentContent(byte[] mainContent, Attachment attachment) {
        this.mainContent = mainContent;
        this.attachment = attachment;
    }


}
