package uz.dizgo.erp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class CategoryTranslate extends AbsEntity {

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Language language;
}
