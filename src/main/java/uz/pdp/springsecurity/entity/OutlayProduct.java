package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outlay_products")
public class OutlayProduct extends AbsEntity {
    private String name;
    private Double price;
    private boolean active;
    @ManyToOne
    private Outlay outlay;
}
