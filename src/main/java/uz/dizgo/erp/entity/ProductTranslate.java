package uz.dizgo.erp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class ProductTranslate extends AbsEntity {
    // Mahsulotning asosiy ma'lumotlari
    @Column(length = 500)
    private String name;                // Mahsulot nomi

    @Column(length = 500)
    private String description;         // Mahsulot haqidagi qisqa tavsif

    @Column(length = 500)
    private String longDescription;     // Mahsulot haqidagi batafsil tavsif

    @Column(length = 500)
    private String keywords;            // Mahsulotga oid kalit so'zlar

    @Column(length = 500)
    private String attributes;          // Qo'shimcha xususiyatlar

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Language language;
}
