package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossGetDto {
    private UUID id;
    private String userName;
    private Date date;
    private String comment; // Qo'shilgan maydon

}
