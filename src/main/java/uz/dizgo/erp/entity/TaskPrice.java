package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskPrice extends AbsEntity {
    @ManyToMany
    private List<User> userList;

    private double price;

    private boolean isEach;


}


