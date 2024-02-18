package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TextbookUser extends AbsEntity {
    @ManyToOne
    private TextBook textBook;

    @ManyToOne
    private User user;

    private int views;

    private boolean isView;
}
