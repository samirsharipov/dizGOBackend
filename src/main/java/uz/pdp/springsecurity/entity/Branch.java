package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "branches")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Where(clause = "deleted = false AND active = true")
public class Branch extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @OneToOne
    private Address address;

    private String addressName;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    private UUID mainBranchId;

    @OneToOne
    private BranchCategory branchCategory;

    public Branch(String name, Address address, Business business) {
        this.name = name;
        this.address = address;
        this.business = business;
    }
}
