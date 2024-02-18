    package uz.pdp.springsecurity.payload;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.util.Date;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class NavigationProcessDto{
        private boolean real;
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
