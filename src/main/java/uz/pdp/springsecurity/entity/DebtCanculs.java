package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "debt_canculs")
public class DebtCanculs extends AbsEntity {
    @OneToOne
    private Trade trade;
    private double dollarPrice;
    private Integer debtPrice;
}