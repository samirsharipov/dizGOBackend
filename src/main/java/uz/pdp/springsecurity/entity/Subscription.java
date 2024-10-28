package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.PayType;
import uz.pdp.springsecurity.enums.StatusTariff;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Subscription extends AbsEntity {

    @ManyToOne
    private Business business;

    @ManyToOne
    private Tariff tariff;

    private Timestamp startDay;

    private Timestamp endDay;

    @Enumerated(EnumType.STRING)
    private StatusTariff statusTariff;

    @Enumerated(EnumType.STRING)
    private PayType payType;


    private boolean activeNewTariff;

    private boolean active;

    private boolean delete;

    private Boolean testDayFinish;

    private Boolean checkTestDay;

    public Subscription(Business business, Tariff tariff, Timestamp startDay, Timestamp endDay, StatusTariff statusTariff, PayType payType, boolean activeNewTariff, boolean active, boolean delete) {
        this.business = business;
        this.tariff = tariff;
        this.startDay = startDay;
        this.endDay = endDay;
        this.statusTariff = statusTariff;
        this.payType = payType;
        this.activeNewTariff = activeNewTariff;
        this.active = active;
        this.delete = delete;
    }
}
