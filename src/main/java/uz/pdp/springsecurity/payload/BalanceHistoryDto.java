package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistoryDto {
    //history vaqti
    private Timestamp date;

    private double summa;

    //otqazdimi yoki oldimi ?
    private boolean plus;

    //hozirda balanceda mavjud summa
    private double accountSumma;

    //otqazilgan yoki olingan summadan keyingi balancedagi holat
    private double totalSumma;

    private UUID balanceId;

    private UUID payMethodId;

    private String payMethodType;

}
