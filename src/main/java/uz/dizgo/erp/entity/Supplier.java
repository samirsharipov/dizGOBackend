package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Supplier extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String telegram;

    private boolean juridical;

    private String companyName;

    private String inn;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    private double debt;

    public Supplier( String name, String phoneNumber, String telegram, Business business) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.business = business;
    }
}
