package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTranslate extends AbsEntity {
    @Column(nullable = false)
    private String name;

    private String description;
    private String longDescription;
    private String keywords;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Language language;
}
