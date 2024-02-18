package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalProductSumDto{
    private double summa;
    private double percentage;
    private String message;
    private boolean success;
}
