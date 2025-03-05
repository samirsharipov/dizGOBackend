package uz.dizgo.erp.payload;

import lombok.Data;

@Data
public class MainPageReport {
    // jami savdo summasi
    private double totalTradeSum;
    // jami savdodan foyda summasi
    private double totalTradeProfitSum;
    // jami savdolar soni
    private long totalTradeCount;
    // mijozlar soni
    private long totalTradeCustomerCount;

    // maxsulotlar soni
    private long totalProductCount;
    // miqdori 0dan katta bolgan maxsulotlar soni
    private long totalProductCountGreaterThanZero;
    // miqdori 0dan kichik bolgan maxsulotlar soni
    private long totalProductCountLessThanZero;
    // Ogohlantirish miqdoriga yetgan maxsulotlar soni
    private long totalProductMinQuantity;

    // userlar soni
    private long countUsers;

    // outlay summasi
    private double outlayTotalSum;

    private long purchaseCount;
}
