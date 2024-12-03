package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
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
@Where(clause = "deleted = false AND active = true")
public class DebtCanculs extends AbsEntity {
    @OneToOne
    private Trade trade;
    private double dollarPrice;
    private Integer debtPrice;
}