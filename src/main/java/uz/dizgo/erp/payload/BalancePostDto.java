package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalancePostDto {
    private Double summa;
    private UUID payMethodId;
    private boolean dollar;
    private String description;
}
