package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PocketMoneyTransaction extends AbsEntity {
    private UUID customerUserId;

    private UUID cashierUserId;

    private double amount;

    private boolean paid = false;
}
