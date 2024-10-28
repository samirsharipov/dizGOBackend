package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends AbsEntity {

    private String fileOriginalName;

    private long size;

    private String contentType;

    private String name;


    @ManyToOne
    private Branch branch;
}
