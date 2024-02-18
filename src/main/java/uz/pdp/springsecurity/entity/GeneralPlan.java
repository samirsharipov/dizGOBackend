package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "general_plans")
public class GeneralPlan extends AbsEntity {
    private Integer tam;
    private Integer sam;
    private Integer som;
    private Integer agentsCount;
    private Date startDate;
    private Date endDate;
    private boolean active;
    @ManyToOne
    private Branch branch;
}
