package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class ProjectStatus extends AbsEntity {

    private String name;

    private String color;

    @ManyToOne
    private Branch branch;

    public ProjectStatus(String name, String color, Branch branch) {
        this.name = name;
        this.color = color;
        this.branch = branch;
    }
}
