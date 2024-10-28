package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.HistoryName;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History extends AbsEntity {
    @Enumerated(value = EnumType.STRING)
    private HistoryName name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Branch branch;

    @Column(nullable = false)
    private String description;
}
