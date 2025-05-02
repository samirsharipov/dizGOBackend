package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.entity.template.ProductActionType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductActivityLog extends AbsEntity {

    private UUID productId;

    @Enumerated(EnumType.STRING)
    private ProductActionType actionType;

    @Column(columnDefinition = "text")
    private String oldData;

    @Column(columnDefinition = "text")
    private String newData;

    @Column(columnDefinition = "text")
    private String extraData;
}
