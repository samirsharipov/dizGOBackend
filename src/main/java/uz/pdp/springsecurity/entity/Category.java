package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
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


    public Category(String name) {
        this.name = name;
    }

    public Category(UUID id, Timestamp createdAt, Timestamp updateAt, Business business) {
        super(id, createdAt, updateAt);
        this.business = business;
    }

    public Category(String name, Business business) {
        this.business = business;
        this.name = name;
    }


    public Category(String name, Business business, String description) {
        this.name=name;
        this.description=description;
        this.business=business;
    }

    public Category(String name, Business business, String description, Category parentCategory) {
        this.name=name;
        this.description=description;
        this.business=business;
        this.parentCategory=parentCategory;
    }

    public Category(String name, String description, UUID parentCategory, Business name1, Category category) {
        this.name=name;
        this.description=description;
        this.business=business;


    }
}