package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TextBook extends AbsEntity {
    @ManyToOne
    private Role role;

    @OneToOne
    private Attachment attachment;

    private int views;

    @ManyToMany
    private List<Test> testList;
}
