package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

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
@Where(clause = "deleted = false AND active = true")
public class TextBook extends AbsEntity {
    @ManyToOne
    private Role role;

    @OneToOne
    private Attachment attachment;

    private int views;

    @ManyToMany
    private List<Test> testList;
}
