package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Language;
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
    private String name;// product name

    private String uniqueSKU;//unique SKU
    private double salePrice;// price sum
    private double salePriceDollar;// price dollar

    @Enumerated(EnumType.STRING)
    private Language language;//language ru,uz,en

    private Double stockAmount;// stock amount
    private Boolean inStock; // in stock (stockda mavjudmi?)
    private Boolean preorder; // Preorder(oldindan buyurtma?)
    private Double length; // uzunligi
    private Double width; // // kengligi
    private Double height; // balandigi
    private Double weight; // og'irligi

    private String hsCode12;
    private String hsCode22;
    private String hsCode32;
    private String hsCode44;

    private String keyWord;
    private String briefDescription;
    private String longDescription;

    private String agreementExportsID;
    private String agreementExportsPID;
    private String agreementLocalID;
    private String agreementLocalPID;
    private String langGroup;
    private String shippingClass;
    private String attributes;
    private Boolean soldIndividualy;

    private Date dueDate;
    private boolean active = true;
    private double profitPercent;
    private double tax = 1;
    private double buyPrice;

    private double grossPrice;
    private double grossPriceDollar;

    @Column(nullable = false, columnDefinition = "numeric default 0")
    private int grossPriceMyControl;

    private double buyPriceDollar;
    private boolean buyDollar;
    private boolean saleDollar;

    private Boolean kpiPercent = true;
    private Double kpi;
    private Date expireDate;

    @Column(nullable = false)
    private String barcode;

    private double minQuantity;

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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category subChildCategory;

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

    private Boolean isGlobal;
}
