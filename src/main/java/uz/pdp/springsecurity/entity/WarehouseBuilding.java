package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class WarehouseBuilding extends AbsEntity {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Branch branch;
    private boolean active;
}
