package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeProductByConfirmation extends AbsEntity {

    @OneToOne
    private ExchangeProductBranch exchangeProductBranch;

    @ManyToOne
    private Car car;

    private Boolean confirmation;

    private String message;
}
