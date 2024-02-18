package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbsEntity {
    @Column(nullable = false)
    private String name;
    private Date dueDate;
    private boolean active = true;
    private double profitPercent;
    private double tax = 1;
    private double buyPrice;
    private double salePrice;
    //    @Column(nullable = false, columnDefinition = "numeric default 0")
//    private double oldSalePrice;
    private double grossPrice;
    private double grossPriceDollar;
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private int grossPriceMyControl;
    private double buyPriceDollar;
    private boolean buyDollar;
    private double salePriceDollar;
    private boolean saleDollar;

    private Boolean kpiPercent = true;
    private Double kpi;
    private Date expireDate;

    @Column(nullable = false)
    private String barcode;

    private double minQuantity;
    //    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Brand brand;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category childCategory;
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Measurement measurement;
    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Branch> branch;
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<WarehouseRasta> rastas = new ArrayList<>();
    private Integer warehouseCount;

    @Transient
    private double quantity;
}
