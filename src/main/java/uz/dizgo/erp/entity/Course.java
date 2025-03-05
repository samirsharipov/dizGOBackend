package uz.dizgo.erp.entity;


import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Course extends AbsEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private Attachment photo;

    @Column(nullable = false)
    private String link;
}
