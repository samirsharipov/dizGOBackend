package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shablon extends AbsEntity {
    private String name;

    @Column(columnDefinition = "text")
    private String message;

    private String originalName;

    @ManyToOne
    private Business business;
}
