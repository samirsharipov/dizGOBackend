package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskStatus extends AbsEntity {
    private String name;
    private String orginalName;
    private int numberOfTask;
    private String color;
    private long rowNumber;
    private boolean aBoolean;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    public TaskStatus(String name, String originalName, int i, boolean b, String hashtag, Branch branch) {
        this.name = name;
        this.orginalName = originalName;
        this.numberOfTask = i;
        this.color = hashtag;
        this.aBoolean = b;
        this.branch = branch;
    }
}
