package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Lifetime;
import uz.pdp.springsecurity.enums.Permissions;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tariff extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Permissions> permissions;

    private int branchAmount;

    private long productAmount;

    private int employeeAmount;

    private long tradeAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Lifetime lifetime;

    private int testDay;

    private int interval;//1 oy 6 oy

    @Column(nullable = false)
    private double price;

    private double discount;

    private boolean isActive;

    private boolean isDelete;
}