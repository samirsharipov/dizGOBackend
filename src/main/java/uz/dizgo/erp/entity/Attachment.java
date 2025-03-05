package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class Attachment extends AbsEntity {

    private String fileOriginalName;

    private long size;

    private String contentType;

    private String name;


    @ManyToOne
    private Branch branch;
}
