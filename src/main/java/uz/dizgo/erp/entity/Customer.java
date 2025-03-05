package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Customer extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String telegram;

    private String description;

    @Column(unique = true, nullable = false)
    private String uniqueCode;

    private String address;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerGroup customerGroup;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Branch> branches;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    private Date birthday;

    private double debt = 0;

    private Date payDate = new Date();

    private Boolean lidCustomer;

    private Double latitude;  // Yangi maydon

    private Double longitude; // Yangi maydon

    private Boolean active = true;

    private Long chatId;

    @ElementCollection
    private Set<UUID> branchIds = new HashSet<>(); // Set bo‘lgani uchun dublikat bo‘lmaydi

    // **Branch ID qo‘shish metodi**
    public void addBranchId(UUID branchId) {
        if (branchIds == null) {
            branchIds = new HashSet<>();
        }
        branchIds.add(branchId); // Set bo‘lgani uchun dublikat oldi olinadi
    }

}