package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Type;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
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
    private Type type;
    @ManyToOne
    private ProductTypePrice productTypePrice;
    @ManyToOne
    private Product product;
    private boolean active;
}
