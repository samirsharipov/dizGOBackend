package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AbsEntity {

    @Column(nullable = false)
    private String name;
    private String phoneNumber;
    private String telegram;
    private String description;
    private String address;

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

    private Double latitude;  // Yangi maydon
    private Double longitude; // Yangi maydon


    private Boolean active = true;

    private Long chatId;
}
