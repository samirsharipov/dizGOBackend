package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BalanceHistory extends AbsEntity {
    //qancha summa otqazmoqchi yoki olmoqchi
    private double summa;

    //otqazdimi yoki oldimi ?
    private boolean plus;

    //hozirda balanceda mavjud summa
    private double accountSumma;

    //otqazilgan yoki olingan summadan keyingi balancedagi holat
    private double totalSumma;

    @ManyToOne
    private Balance balance;
}
