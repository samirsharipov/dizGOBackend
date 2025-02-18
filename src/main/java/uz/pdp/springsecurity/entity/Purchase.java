package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Purchase extends AbsEntity {
    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private User seller;
    @ManyToOne
    private ExchangeStatus purchaseStatus;
    @ManyToOne
    private PaymentStatus paymentStatus;
    @ManyToOne
    private PaymentMethod paymentMethod;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    private Date date;
    private String description;
    private String invoice;
    private double deliveryPrice;
    private double totalSum;
    private double paidSum;
    private double debtSum = 0;
    private boolean editable = true;

    public String getBranchName() {
        return this.branch != null ? this.branch.getName() : null;
    }

    // Supplier nomini olish uchun method
    public String getSupplierName() {
        return this.supplier != null ? this.supplier.getName() : null;
    }

    // Seller nomini olish uchun method
    public String getSellerFullName() {
        return this.seller != null ? this.seller.getFirstName() + " " + this.seller.getLastName() : null;
    }
}
