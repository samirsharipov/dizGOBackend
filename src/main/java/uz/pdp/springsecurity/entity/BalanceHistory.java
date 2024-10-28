package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
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

    private String description;

    private String currency;
}
