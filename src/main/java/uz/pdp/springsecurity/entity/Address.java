package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbsEntity {
    @Column(nullable = false)
    private String city;

    private String district;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String home;
}
