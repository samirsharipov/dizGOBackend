package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeProduct extends AbsEntity {

    private Double exchangeProductQuantity;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
}
