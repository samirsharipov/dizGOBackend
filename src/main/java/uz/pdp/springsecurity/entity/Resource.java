package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
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
