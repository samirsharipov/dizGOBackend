package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class ExchangeProductBranch extends AbsEntity {

    @OneToOne
    private Branch shippedBranch;

    @ManyToOne
    private Branch receivedBranch;

    private Date exchangeDate;

    private String description;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private List<ExchangeProduct> exchangeProductList;

    @OneToOne
    private ExchangeStatus exchangeStatus;

    @ManyToOne
    private Business business;

    private Boolean delete;
}
