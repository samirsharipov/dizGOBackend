package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProjectStatus{
    @Id
    @GeneratedValue
    UUID id;
    private String name;
    private String color;
    @ManyToOne
    private Branch branch;
}
