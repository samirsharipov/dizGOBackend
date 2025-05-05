package uz.dizgo.erp.entity.template;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Where(clause = "deleted = false AND active = true")
@EntityListeners(AuditingEntityListener.class)
public abstract class AbsEntity implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    @CreatedBy
    @Column(nullable = true)
    private UUID createdBy;

    @LastModifiedBy
    @Column(nullable = true)
    private UUID lastModifiedBy;

    private boolean deleted = Boolean.FALSE;

    private boolean active = Boolean.TRUE;
}
