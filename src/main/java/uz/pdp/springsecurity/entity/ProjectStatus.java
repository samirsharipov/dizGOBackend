package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
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
