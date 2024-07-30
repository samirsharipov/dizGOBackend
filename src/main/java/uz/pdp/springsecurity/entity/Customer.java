package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer extends AbsEntity {

    @Column(nullable = false)
    private String name;
    private String phoneNumber;
    private String telegram;
    private String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerGroup customerGroup;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Branch> branches;

    private Date birthday;

    private double debt = 0;

    private Date payDate = new Date();

    private Boolean lidCustomer;

    private Boolean active = true;

    private Long chatId;

    public Customer(String name, String phoneNumber, String telegram, Business business) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.business = business;

    }

    public Customer(UUID id, Timestamp createdAt, Timestamp updateAt, String name, String phoneNumber, String telegram, CustomerGroup customerGroup, Business business, Double debt) {
        super(id, createdAt, updateAt);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.customerGroup = customerGroup;
        this.business = business;
        this.debt = debt;
    }

    public Customer(UUID id, Timestamp createdAt, Timestamp updateAt, String name, String phoneNumber, String telegram, CustomerGroup customerGroup, Business business) {
        super(id, createdAt, updateAt);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.customerGroup = customerGroup;
        this.business = business;
    }


    public Customer(String name, String phoneNumber, String telegram, Business business, Branch branch) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.business = business;
        this.branch = branch;

    }

    public Customer(String name, String phoneNumber, String telegram, Business business, Branch branch, CustomerGroup customerGroup) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.business = business;
        this.branch = branch;
        this.customerGroup = customerGroup;
    }
}
