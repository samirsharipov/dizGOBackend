package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WarehouseSector extends AbsEntity {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private WarehouseFloor floor;
    private boolean active;
}
