package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOutlayCategory extends AbsEntity {
    private String name;
    @ManyToOne
    private Business business;

    private boolean deleted = false;
}
