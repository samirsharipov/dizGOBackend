    package uz.pdp.springsecurity.entity;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.OnDelete;
    import org.hibernate.annotations.OnDeleteAction;
    import uz.pdp.springsecurity.entity.template.AbsEntity;

    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.ManyToOne;
    import java.util.Date;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public class Navigation extends AbsEntity {
        @ManyToOne(optional = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Branch branch;

        @Column(nullable = false)
        private double initial;

        @Column(nullable = false)
        private double goal;

        @Column(nullable = false)
        private Date startDate;

        @Column(nullable = false)
        private Date endDate;

        private String description;
    }
