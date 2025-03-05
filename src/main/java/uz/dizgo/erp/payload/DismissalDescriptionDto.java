package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DismissalDescriptionDto {
    private UUID id;
    private String description;
    private UUID businessId;
    private boolean mandatory;
}
