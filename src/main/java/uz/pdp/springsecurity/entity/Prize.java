package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class Prize extends AbsEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bonus bonus;

    @Column(nullable = false)
    private Date date;

    private String description;

    @Column(nullable = false)
    private boolean given;

    @Column(nullable = false)
    private boolean active = true;

    private boolean task;
    private boolean lid;
    private Integer count;
    private Date deadline = new Date();
    private Integer counter = 0;

    public Prize(Branch branch, User user, Bonus bonus, Date date, String description, boolean given) {
        this.branch = branch;
        this.user = user;
        this.bonus = bonus;
        this.date = date;
        this.description = description;
        this.given = given;
    }
}
