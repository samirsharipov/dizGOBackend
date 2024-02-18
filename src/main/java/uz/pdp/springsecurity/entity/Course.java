package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
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
