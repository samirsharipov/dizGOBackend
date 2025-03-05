package uz.dizgo.erp.entity;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
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
