package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
