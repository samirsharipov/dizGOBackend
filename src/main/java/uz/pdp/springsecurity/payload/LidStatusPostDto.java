package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidStatusPostDto {
    private UUID id;
    private String name;
    private String color;
    private Integer sort;
    private boolean increase;
    private boolean saleStatus;
    private UUID businessId;
}
