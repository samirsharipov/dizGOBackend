package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.DiscountType;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Discount extends AbsEntity {

    private String name; // Chegirma nomi
    private String description; // Ixtiyoriy tavsif
    private DiscountType type; // Chegirma turi (foiz yoki summa)
    private double value; // Chegirma miqdori

    private Timestamp startDate; // Boshlanish sanasi
    private Timestamp endDate; // Tugash sanasi

    @ManyToMany
    private List<Product> products; // Bog'langan mahsulotlar

    @ManyToMany
    private List<Branch> branches; // Bog'langan filliallar
}
