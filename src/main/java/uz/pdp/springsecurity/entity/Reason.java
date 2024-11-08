package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "active = true")
@Table(indexes = {@Index(columnList = "businessId")})
public class Reason extends AbsEntity {
    private String name;

    private UUID businessId;

    private Boolean active = true;

    public Reason(UUID id, String name, UUID businessId) {
        super.setId(id);  // `AbsEntity`dan meros olingan `id` ni o'rnatish
        this.name = name;
        this.businessId = businessId;
    }
}
