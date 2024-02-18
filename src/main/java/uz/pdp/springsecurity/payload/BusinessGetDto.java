package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessGetDto {
    private UUID id;
    private String name;
    private String description;
    private boolean isActive;
    private boolean isDelete;
}
