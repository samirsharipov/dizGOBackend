package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeGetOneDto {
    private Trade trade;
    private List<TradeProduct> tradeProductList;
    private List<PaymentDto> paymentDtoList;
}
