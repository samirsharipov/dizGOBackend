package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class ProductTypePrice extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductTypeValue productTypeValue; //oq

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductTypeValue subProductTypeValue;//32,64,128,256

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;

    private String barcode;

    private double buyPrice;

    private double salePrice;
    private double buyPriceDollar = 1;
    private double salePriceDollar = 1;
    private double grossPrice;
    private double grossPriceDollar;
//    @Column(nullable = false, columnDefinition = "numeric default 0")
//    private double oldSalePrice;
    private double profitPercent;
    private Boolean active;
    private Integer warehouseCount;
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<WarehouseRasta> rastas = new ArrayList<>();
}
