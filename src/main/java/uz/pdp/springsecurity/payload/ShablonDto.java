package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShablonDto {
    private UUID id;
    private String name;
    private String message;
    private UUID businessId;
    private String originalName;
}
