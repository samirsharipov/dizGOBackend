package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Production extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private double totalQuantity;

    @Column(nullable = false)
    private double quantity = 0;

    @Column(nullable = false)
    private double invalid = 0d;

    @Column(nullable = false)
    private double totalPrice = 0;

    @Column(nullable = false)
    private double contentPrice;

    @Column(nullable = false)
    private double cost;

    private Double taskPrice;

    @Column(nullable = false)
    private boolean costEachOne;

    private boolean done = true;

    @Column(length = 1000)
    private String description ;

    @Column(nullable = false)
    private String status; // "1-smen", "2-smen", "3-smen" va hokazo

    public Production(Branch branch, Date date, double totalQuantity, double quantity, double invalid, double totalPrice, double contentPrice, double cost, boolean costEachOne) {
        this.branch = branch;
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.quantity = quantity;
        this.invalid = invalid;
        this.totalPrice = totalPrice;
        this.contentPrice = contentPrice;
        this.cost = cost;
        this.costEachOne = costEachOne;
    }
    public Production(Branch branch, Date date, double totalQuantity, double quantity, double invalid, double totalPrice, double contentPrice, double cost, boolean costEachOne,String description, String status) {
        this.branch = branch;
        this.date = date;
        this.totalQuantity = totalQuantity;
        this.quantity = quantity;
        this.invalid = invalid;
        this.totalPrice = totalPrice;
        this.contentPrice = contentPrice;
        this.cost = cost;
        this.costEachOne = costEachOne;
        this.description=description;
        this.status=status;

    }
}
