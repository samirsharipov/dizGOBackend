package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLessProductDto {
    private String name;
    private double amount;
    private Date lastSoldDate;
    private UUID attachmentId;
    private double money;
}
