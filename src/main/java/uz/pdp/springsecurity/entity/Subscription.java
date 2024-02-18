package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.PayType;
import uz.pdp.springsecurity.enums.StatusTariff;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
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
}
