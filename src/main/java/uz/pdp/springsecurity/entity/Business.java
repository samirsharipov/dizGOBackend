package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Where;
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
@Where(clause = "deleted = false AND active = true")
public class Business extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private boolean saleMinus = false;

    @Column(unique = true)
    private String businessNumber;

    private boolean isActive;

    private boolean delete;

    private String status; // active, blocked, archive

    private Boolean exchangeProductByConfirmation;

    @Column(name = "cheap_selling_price", columnDefinition = "boolean default false", nullable = false)
    private boolean cheapSellingPrice;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean grossPriceControl;

    private int editDays = 30;

    private Boolean main = Boolean.FALSE;

    public Business(String name, String description, boolean active, boolean delete, boolean main) {
        this.name = name;
        this.description = description;
        this.isActive = active;
        this.delete = delete;
        this.main = main;
    }
}
