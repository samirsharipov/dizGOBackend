package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod extends AbsEntity {
    private String type;

    private boolean card;

    private boolean cash;
}