package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatus extends AbsEntity {

//    @Column(nullable = false, unique = true)
//    @Enumerated(value = EnumType.STRING)
//    private StatusName statusName;

    private String status;

}
