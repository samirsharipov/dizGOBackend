package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoDto {
    private CustomerDto customerDto;
    private Double totalTradeSum;
    private Double totalProfitSum;
}
