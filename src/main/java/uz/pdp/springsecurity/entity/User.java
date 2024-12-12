package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Permissions;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
@Where(clause = "deleted = false AND active = true")
public class User extends AbsEntity implements UserDetails {

    @Column(nullable = false)
    private String firstName; // Foydalanuvchining ismi (bo'sh bo'lishi mumkin emas)

    @Column(nullable = false)
    private String lastName; // Foydalanuvchining familiyasi (bo'sh bo'lishi mumkin emas)

    private String sureName; // Foydalanuvchining otasining ismi (optional)

    private boolean sex; // Foydalanuvchining jinsi (true - erkak, false - ayol)

    private Date birthday; // Foydalanuvchining tug‘ilgan sanasi

    private String email; // Foydalanuvchining elektron pochtasi (optional)

    @Column(unique = true)
    private String phoneNumber; // Foydalanuvchining telefon raqami (takrorlanmas bo‘lishi kerak)

    @Column(nullable = false, unique = true)
    private String username; // Foydalanuvchining login nomi (bo'sh va takror bo'lishi mumkin emas)

    @Column(nullable = false)
    private String password; // Foydalanuvchining paroli (bo'sh bo'lishi mumkin emas)

    private String passportNumber; // Foydalanuvchining pasport raqami (optional)

    private String jshshsr; // Foydalanuvchining JSHSHIR raqami (optional)

    private String address; // Foydalanuvchining yashash manzili (optional)

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Attachment photo; // Foydalanuvchining fotosurati (attachment fayli)

    private String department; // Foydalanuvchining ishlayotgan bo‘limi (optional)

    private String position; // Foydalanuvchining lavozimi (masalan, direktor, manager, ishchi)

    private String branch; // Foydalanuvchining qaysi filialga tegishli ekanligi

    private String arrivalTime; // Foydalanuvchining kelish vaqti (format: HH:mm)

    private String leaveTime; // Foydalanuvchining ketish vaqti (format: HH:mm)

    private Double salaryAmount; // Foydalanuvchining oylik ish haqi miqdori

    private String salaryType; // Ish haqi turi (masalan, soatbay, oylik, haftalik)

    private String pinCode; // Foydalanuvchining maxsus pin-kodi (himoya qilish uchun)

    private String shiftType; // Foydalanuvchining smena turi (kunduzgi, tungi, 2 smenali va h.k.)

    private Date probation; // Sinov muddati tugash sanasi (optional)

    @ManyToOne
    private Job job; // Foydalanuvchining ishlayotgan lavozimi (Job entitasiga bog‘liq)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private Role role; // Foydalanuvchining roli (ADMIN, USER, MANAGER va h.k.)

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business; // Foydalanuvchiga tegishli biznes

    @ManyToMany
    @ToString.Exclude
    private Set<Branch> branches; // Foydalanuvchi qaysi filiallarga tegishli ekanligini bildiradi

    private boolean active; // Foydalanuvchining faol (true) yoki faol emas (false) ekanligini ko‘rsatadi

    private String description; // Foydalanuvchi haqidagi qo‘shimcha ma’lumot (optional)

    @ManyToMany
    @ToString.Exclude
    private List<Bonus> bonuses; // Foydalanuvchiga berilgan bonuslar ro‘yxati

    private Date dateOfEmployment; // Foydalanuvchining ishga qabul qilingan sanasi

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean grossPriceControlOneUser; // Bu foydalanuvchi uchun narxni boshqarish uchun maxsus bayroq (aniq ma'nosi noaniq)

    private String contractNumber; // Shartnoma raqami

    @ManyToOne
    private Attachment contractFile; // Shartnoma fayli (Attachment)

    @ElementCollection
    private List<EmergencyContact> emergencyContacts;


    private Long chatId; // Telegram chat identifikatori (Telegram bot uchun ishlatilishi mumkin)

    private boolean enabled = false; // Foydalanuvchining akkaunti faol yoki faol emasligini ko‘rsatadi (false bo'lsa, akkaunt bloklanadi)

    private boolean accountNonExpired = true; // Foydalanuvchining akkaunt muddati tugagan yoki tugamaganligini bildiradi

    private boolean accountNonLocked = true; // Foydalanuvchining akkaunti qulflangan (true - qulflanmagan, false - qulflangan)

    private boolean credentialsNonExpired = true; // Foydalanuvchining paroli muddati tugagan yoki yo‘qligini bildiradi

    /**
     * Parametrli konstruktor
     */
    public User(String firstName, String lastName, String username, String password, Role role, boolean enabled, Business business, Set<Branch> branches, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.business = business;
        this.branches = branches;
        this.active = active;
    }

    /**
     * UserDetails interfeysidan olingan usul.
     * Bu usul foydalanuvchining rollari va ruxsatlarini qaytaradi.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Permissions> permissions = this.role.getPermissions(); // Rolga tegishli ruxsatlarni olish
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Permissions permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission.name())); // Ruxsatni GrantedAuthority ga o‘girish
        }
        return grantedAuthorities; // Foydalanuvchining ruxsatlari ro‘yxati qaytariladi
    }
}