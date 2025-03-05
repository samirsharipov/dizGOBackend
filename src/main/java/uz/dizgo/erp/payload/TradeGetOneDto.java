package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dizgo.erp.entity.Trade;
import uz.dizgo.erp.entity.TradeProduct;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeGetOneDto {
    private Trade trade;
    private List<TradeProduct> tradeProductList;
    private List<PaymentDto> paymentDtoList;
}
