package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class TextbookUser extends AbsEntity {
    @ManyToOne
    private TextBook textBook;

    @ManyToOne
    private User user;

    private int views;

    private boolean isView;
}
