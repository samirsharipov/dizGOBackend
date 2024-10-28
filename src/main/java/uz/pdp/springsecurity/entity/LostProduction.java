package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LostProduction extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskStatus taskStatus;

    private double quantity = 0;
}


