package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbsEntity {

    // Mahsulotning asosiy ma'lumotlari
    private String name;                // Mahsulot nomi
    private String description;         // Mahsulot haqidagi qisqa tavsif
    private String longDescription;     // Mahsulot haqidagi batafsil tavsif
    private String keywords;            // Mahsulotga oid kalit so'zlar
    private String attributes;          // Qo'shimcha xususiyatlar

    // Mahsulot tarjimalari
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductTranslate> translations; // Mahsulotga oid tarjimalar ro'yxati

    // Mahsulotning boshqa ma'lumotlari
    private String uniqueSKU;           // Unikal SKU (Stock Keeping Unit)

    // Narxlar
    private double salePrice;           // Mahsulotning sotish narxi (so'm)
    private double salePriceDollar;     // Mahsulotning sotish narxi (dollar)

    // Inventar ma'lumotlari
    private Double stockAmount;         // Mavjud inventar miqdori
    private Boolean inStock;            // Mahsulot inventarda mavjudligi
    private Boolean preorder;           // Oldindan buyurtma berish holati
    private Double length;              // Mahsulot uzunligi
    private Double width;               // Mahsulot kengligi
    private Double height;              // Mahsulot balandligi
    private Double weight;              // Mahsulot og'irligi

    // Harmonizatsiya kodlari
    private String hsCode12;            // 12-harmonizatsiya kodi
    private String hsCode22;            // 22-harmonizatsiya kodi
    private String hsCode32;            // 32-harmonizatsiya kodi
    private String hsCode44;            // 44-harmonizatsiya kodi

    // Shartnomalar bilan bog'liq ma'lumotlar
    private String agreementExportsID;  // Eksport shartnomasi ID
    private String agreementExportsPID; // Eksport shartnomasi PID
    private String agreementLocalID;    // Mahalliy shartnoma ID
    private String agreementLocalPID;   // Mahalliy shartnoma PID
    private String langGroup;           // Til guruhi identifikatori
    private String shippingClass;       // Yuk tashish klassifikatsiyasi
    private Boolean soldIndividually;   // Mahsulotni alohida sotish holati

    // Boshqa ma'lumotlar
    private Date dueDate;               // Muddat
    private boolean active = true;      // Faol yoki yo'qligini bildiruvchi holat
    private double profitPercent;       // Foyda foizi
    private double tax = 1;             // Soliq foizi
    private double buyPrice;            // Sotib olish narxi

    // Boshqa narxlar
    private double grossPrice;          // Sof narx
    private double grossPriceDollar;    // Sof narx (dollar)

    @Column(nullable = false, columnDefinition = "numeric default 0")
    private int grossPriceMyControl;    // Maxsus sof narx

    private double buyPriceDollar;       // Sotib olish narxi (dollar)
    private boolean buyDollar;           // Dollar bilan sotib olish holati
    private boolean saleDollar;          // Dollar bilan sotish holati

    private Boolean kpiPercent = true;   // KPI foiz sifatida
    private Double kpi;                  // KPI qiymati
    private Date expireDate;             // Muddati o'tgan sana

    @Column(nullable = false)
    private String barcode;              // Mahsulotning barkodi

    private double minQuantity;          // Sotish uchun minimal miqdor

    // Bog'lanishlar
    @ManyToOne
    private Brand brand;                 // Brend ma'lumotlari

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;           // Kategoriya ma'lumotlari

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Measurement measurement;     // O'lchov birligi

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;            // Mahsulotning fotosurati

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;           // Biznes ma'lumotlari

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Branch> branch;         // Mahsulot mavjud bo'lgan filiallar

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<WarehouseRasta> rastaList = new ArrayList<>(); // Ombor joylari

    private Integer warehouseCount;      // Ombordagi mahsulotlar soni

    @Transient
    private double quantity;             // Mahsulot miqdori (temporarily used)

    private Boolean isGlobal;            // Global mavjudlik
    private boolean main;                // Asosiy mahsulot ko'rsatkichi

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}