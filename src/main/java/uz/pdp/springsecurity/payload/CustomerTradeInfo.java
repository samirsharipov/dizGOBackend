package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTradeInfo {
    private Timestamp createAt;
    private List<TradeProductCustomerDto> productCutomerDtoList;
    private Double totalSumma;
    private boolean isTrade;
    private boolean isPaid;
}
