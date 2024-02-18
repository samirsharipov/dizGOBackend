package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.ProductTypePrice;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryGetDto{
    private String name;
    private String barcode;
    private String measurement;
    Date date;
    private double amount;
    private double plusAmount;
    private double minusAmount;
}
