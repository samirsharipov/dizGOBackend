package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRDataDto {
    private UUID branchId;
    private String branchName;
    private UUID userId;
    private String userName;
    private String ePosCode;
}
