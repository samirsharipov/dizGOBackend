package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
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
