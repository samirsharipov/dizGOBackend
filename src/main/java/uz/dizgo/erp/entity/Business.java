package uz.dizgo.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Business extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private boolean saleMinus = false;

    @Column(unique = true)
    private String businessNumber;

    private String addressHome;

    private String status; // active, blocked, archive

    private Boolean exchangeProductByConfirmation;

    @Column(name = "cheap_selling_price", columnDefinition = "boolean default false", nullable = false)
    private boolean cheapSellingPrice;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean grossPriceControl;

    private int editDays = 30;

    private Boolean main = Boolean.FALSE;

    private Timestamp contractStartDate;

    private Timestamp contractEndDate;

    public Business(String name, String description, boolean active, boolean delete, boolean main) {
        this.name = name;
        this.description = description;
        super.setActive(active);
        super.setDeleted(delete);
        this.main = main;
    }
}
