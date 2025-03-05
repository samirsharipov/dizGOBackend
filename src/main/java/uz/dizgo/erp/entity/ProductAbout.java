package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductAbout extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @Column(nullable = false)
    private String description;

    private double amount;
    private UUID tradeId;
}
