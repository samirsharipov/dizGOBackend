package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class Loss extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = true) // Komponent null bo'lishi mumkin
    private String comment; // Yangi qo'shilgan maydon

    private double price;

    private boolean editable;

    public Loss(User user, Branch branch, Date date, String comment) {
        this.user = user;
        this.branch = branch;
        this.date = date;
        this.comment = comment; // Konstruktor orqali qabul qilish

    }
}