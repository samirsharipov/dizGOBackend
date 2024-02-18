package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class WorkTime extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    private Timestamp arrivalTime;

    private Timestamp leaveTime;

    private long minute;

    private String description;

    private boolean active;

    public WorkTime(Branch branch, User user, Timestamp arrivalTime, Timestamp leaveTime, boolean active) {
        this.branch = branch;
        this.user = user;
        this.arrivalTime = arrivalTime;
        this.leaveTime = leaveTime;
        this.active = active;
    }
}
