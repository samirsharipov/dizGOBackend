package uz.pdp.springsecurity.entity;

import lombok.Getter;
import lombok.Setter;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class AddressTranslate extends AbsEntity {

    private String name;

    @ManyToOne
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;
}