package uz.dizgo.erp.entity;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Lesson extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Integer view;

    @ManyToOne(cascade = CascadeType.ALL)
    private Attachment attachment;

    @Column(nullable = false)
    private boolean hasTest = false;

    private String description;
}
