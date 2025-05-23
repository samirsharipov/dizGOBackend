package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class
BranchCategory extends AbsEntity {

    private String name;

    private String description;
}
