package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
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
