package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Cash extends AbsEntity {
    @Column(nullable = false)
    private String name; //  Kassa n : Kassa 1, Kassa 2

    @Column(nullable = false)
    private UUID branchId;
}
