package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends AbsEntity {
    @OneToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(unique = true)
    private Branch branch;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private Attachment photo;

    @Column(nullable = false)
    private String footer;

    public Invoice(Branch branch, String name, String description, String footer) {
        this.branch = branch;
        this.name = name;
        this.description = description;
        this.footer = footer;
    }
}
