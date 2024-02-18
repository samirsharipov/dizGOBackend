package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStaticDto {
    private TotalSumDto totalSumDto;
    private TotalProfitSumDto totalProfitSumDto;
    private TotalCustomerDto totalCustomerDto;
    private TotalProductSumDto totalProductSumDto;
}
