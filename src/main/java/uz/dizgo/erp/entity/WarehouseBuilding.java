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
@Builder
@Entity
public class WarehouseBuilding extends AbsEntity {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Branch branch;
    private boolean active;
}
