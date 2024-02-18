package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoOutlayDto {
    private HashMap<Integer, Double> outlay;
    private HashMap<Integer, Double> trade;
    List<Timestamp> timestampList;
    private List<Double> purchasePriceList;
    private List<String> purchaseDateList;
    private List<Integer> purchaseCountList;
}
