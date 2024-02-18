package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WarehouseRasta extends AbsEntity {
    @Column(nullable = false)
    private String name;
    private boolean active;
    @ManyToOne
    private WarehouseSector sector;
}
