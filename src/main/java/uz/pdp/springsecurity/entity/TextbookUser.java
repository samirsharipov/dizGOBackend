package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
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
