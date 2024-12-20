package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class PaymentMethod extends AbsEntity {
    private String type;

    private boolean card;

    private boolean cash;
}