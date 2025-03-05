package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGrossPriceControlDto {
    UUID userId;
    String firstName;
    String lastName;
    Boolean grossPriceControlOneUser;
}
