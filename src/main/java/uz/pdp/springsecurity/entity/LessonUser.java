package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonUser extends AbsEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lesson lesson;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Integer lessonView;

    @Column(nullable = false)
    private Integer view = 0;

    @Column(nullable = false)
    private boolean finish = false;

    @Column(nullable = false)
    private boolean solveTest = false;

    @Column(nullable = false)
    private Integer testResult = 0;

    public LessonUser(Lesson lesson, User user, Integer lessonView) {
        this.lesson = lesson;
        this.user = user;
        this.lessonView = lessonView;
    }
}
