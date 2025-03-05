package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.Lifetime;
import uz.dizgo.erp.enums.Permissions;

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