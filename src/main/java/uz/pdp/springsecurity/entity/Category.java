package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Category extends AbsEntity {

    private String name;

    private String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    @ManyToOne
    @JoinColumn(name = "parent_category")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parentCategory;

    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryTranslate> translations = new ArrayList<>();

    public Category(Business business, String name, boolean active, boolean delete) {
        this.business = business;
        this.name = name;
        super.setActive(active);
        super.setDeleted(delete);
    }
}