    package uz.pdp.springsecurity.entity;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.OnDelete;
    import org.hibernate.annotations.OnDeleteAction;
    import uz.pdp.springsecurity.entity.template.AbsEntity;

    import javax.persistence.Entity;
    import javax.persistence.ManyToOne;
    import javax.persistence.OneToOne;
    import java.util.Date;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public class NavigationProcess extends AbsEntity {
        @ManyToOne(optional = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        private Branch branch;
        private boolean real = false;
        private Date date;

        double totalSell;
        double seller;
        double averageSell;

        double product;
        double productBuyPrice;
        double productSalePrice;

        double producedProductAmount;
        double producedProductPrice;

        double customer;
        double lid;
        double lidPrice;
        double salary;
        double totalUser;

        double outlay;
    }

