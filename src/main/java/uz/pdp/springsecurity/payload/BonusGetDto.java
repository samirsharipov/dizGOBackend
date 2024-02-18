package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusGetDto {
    private UUID Id;

    private String name;

    private String color;

    private String icon;

    private double summa;

    private boolean active;
}
