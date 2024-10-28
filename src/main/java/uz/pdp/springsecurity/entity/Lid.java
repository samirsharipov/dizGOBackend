package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lid extends AbsEntity {

    @ElementCollection
    private Map<LidField, String> values;

    @ManyToOne
    private LidStatus lidStatus;

    @ManyToOne
    private Business business;

    @ManyToOne
    private Source source;

    private String description;

    private boolean delete;

    private String userName;
}
