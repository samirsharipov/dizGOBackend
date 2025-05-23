package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Resource extends AbsEntity {
    private String name;

    private String description;

    private Double percentage;

    private Double totalSum;

    private Date startDate;

    private Date endDate;

    private Double dailyAmount;

    private Date lastUpdateDate;

    @ManyToOne
    private Branch branch;

    private Boolean active = true;
}
