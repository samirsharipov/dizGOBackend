package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DismissalDto {
    private UUID id;
    private UUID userid;
    private UUID dismissalDescriptionId;
    private String comment;
}
