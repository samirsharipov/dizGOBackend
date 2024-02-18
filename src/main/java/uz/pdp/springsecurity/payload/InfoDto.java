package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Customer;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoDto {
    private double myDebt;
    private double myPurchase;
    private double myTrade;
    private double tradersDebt;
    private double myOutlay;
    private double totalProfit;
    private double todayProfit;

    private List<OutlayGetCategory> outlayByCategory;

    private HashMap<String, Double> byPayMethods;

    List<Customer> customerList;
}
