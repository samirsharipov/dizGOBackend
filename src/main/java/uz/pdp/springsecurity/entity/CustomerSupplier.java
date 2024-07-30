package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerSupplier extends AbsEntity {
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    private Supplier supplier;

    private double balance;
}
