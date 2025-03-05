package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "agent_plans")
public class AgentPlan extends AbsEntity {
    private Integer plan;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    private Branch branch;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;
    private boolean active;
}
