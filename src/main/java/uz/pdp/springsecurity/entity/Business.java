package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

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

    private Boolean main = Boolean.FALSE;

    public Business(String name, String description, boolean active, boolean delete, boolean main) {
        this.name = name;
        this.description = description;
        this.isActive = active;
        this.delete = delete;
        this.main = main;
    }
}
