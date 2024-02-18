package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidStatusDto {
    private UUID id;
    private String name;
    private String color;
    private String orginalName;
    private Integer sort;
    private boolean increase;
    private int numberOfLids;
    private UUID businessId;
}
