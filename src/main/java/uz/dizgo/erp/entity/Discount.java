package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.DiscountType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "1=1")
public class Discount extends AbsEntity {

    private String name; // Chegirma nomi
    private String description; // Ixtiyoriy tavsif
    private DiscountType type; // Chegirma turi (foiz yoki summa)
    private double value; // Chegirma miqdori

    private Timestamp startDate; // Boshlanish sanasi
    private Timestamp endDate; // Tugash sanasi

    private Time startHour; // Kun davomida chegirma boshlanish soati
    private Time endHour; // Kun davomida chegirma tugash soati

    private Boolean isWeekday; // Hafta kunlarida faollikni tekshirish uchun
    private Boolean isTime; // Soatlarni tekshirish uchun

    @ElementCollection
    private Set<Integer> weekDays; // Chegirma amal qiladigan hafta kunlari (1 = Dushanba, 7 = Yakshanba)

    @ManyToMany
    private List<Product> products; // Bog'langan mahsulotlar

    @ManyToMany
    private List<Branch> branches; // Bog'langan filliallar
}