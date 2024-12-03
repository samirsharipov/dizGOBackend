package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class EmploymentDismissalReport extends AbsEntity {
    private UUID businessId;

    @Column(nullable = false)
    private long activeUsersCount; // Ishlayotganlar soni

    @Column(nullable = false)
    private long dismissedUsersCount; // Bo'shatilganlar soni
}
