package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "branches")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Branch extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @OneToOne
    private Address address;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

}
