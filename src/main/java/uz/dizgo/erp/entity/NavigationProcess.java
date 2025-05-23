    package uz.dizgo.erp.entity;

    import lombok.*;
    import org.hibernate.annotations.OnDelete;
    import org.hibernate.annotations.OnDeleteAction;
    import uz.dizgo.erp.entity.template.AbsEntity;

    import javax.persistence.Entity;
    import javax.persistence.ManyToOne;
    import java.util.Date;

    @Getter
    @Setter
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

