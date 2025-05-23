package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.SalaryStatus;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Agreement extends AbsEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryStatus salaryStatus;

    @Column(nullable = false)
    private double price;

    private boolean active;

    @Column(nullable = false)
    private Date startDate = new Date();

    @Column(nullable = false)
    private Date endDate = new Date();

    public Agreement(User user, SalaryStatus salaryStatus, double price, boolean active) {
        this.user = user;
        this.salaryStatus = salaryStatus;
        this.price = price;
        this.active = active;
    }
}


