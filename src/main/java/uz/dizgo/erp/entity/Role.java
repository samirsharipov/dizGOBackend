package uz.dizgo.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.Permissions;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Where(clause = "deleted = false AND active = true")
public class Role extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Permissions> permissions;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    @ManyToOne
    private Role parentRole;

    private String description;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isOffice;

    @ManyToOne
    private RoleCategory roleCategory;

    public Role(String name, List<Permissions> permissions, Business business) {
        this.name = name;
        this.permissions = permissions;
        this.business = business;
    }

    public Role(String name, List<Permissions> permissions, Business business, Role parentRole) {
        this.name = name;
        this.permissions = permissions;
        this.business = business;
        this.parentRole = parentRole;
    }

    public Role(String name, List<Permissions> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}