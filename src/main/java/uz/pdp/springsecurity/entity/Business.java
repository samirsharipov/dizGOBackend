package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Business extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    // minusga sotish
    private boolean saleMinus = false;

    private boolean isActive;

    private boolean delete;

    private Boolean exchangeProductByConfirmation;

    @Column(name = "cheap_selling_price", columnDefinition = "boolean default false", nullable = false)
    private boolean cheapSellingPrice;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean grossPriceControl;

    private int editDays = 30; // default 30 days
}
