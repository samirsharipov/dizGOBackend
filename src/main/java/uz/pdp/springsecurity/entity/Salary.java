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
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Salary extends AbsEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    private double remain = 0;

    private double salary = 0;

    private double payedSum = 0;

    /**
     * don't touch it is true
     */
    private boolean active = true;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    private String description;

    @Transient
    private double shouldPaySum = 0;

    public Salary(User user, Branch branch, Date startDate, Date endDate) {
        this.user = user;
        this.branch = branch;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Salary(User user, Branch branch, double remain, Date startDate, Date endDate) {
        this.user = user;
        this.branch = branch;
        this.remain = remain;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Salary(User user, Branch branch, double remain, double salary, Date startDate, Date endDate) {
        this.user = user;
        this.branch = branch;
        this.remain = remain;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public double getShouldPaySum() {
        return ((this.remain + this.salary) - this.payedSum);
    }
}
