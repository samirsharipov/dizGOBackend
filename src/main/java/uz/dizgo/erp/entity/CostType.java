package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class CostType extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    private boolean delete = false;

    public CostType(String name, Branch branch) {
        this.name = name;
        this.branch = branch;
    }
}
