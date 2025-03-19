package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PocketMoneyDto {

    private UUID customerUserId;

    private UUID cashierUserId;

    private double amount;
}
