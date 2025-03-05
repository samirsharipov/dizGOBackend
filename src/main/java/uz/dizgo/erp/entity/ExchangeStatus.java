package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeStatus  extends AbsEntity {

    @Column(nullable = false,unique = true)
    private String status;
}
