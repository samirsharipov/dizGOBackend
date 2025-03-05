package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Form extends AbsEntity {
    @ManyToMany
    private List<LidField> lidFields;

    @ManyToOne
    private Source source;

    @ManyToOne
    private Business business;
}
