package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Permissions;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
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

    public void addPermission(Permissions permissions){
        this.permissions.add(permissions);
    }

}
