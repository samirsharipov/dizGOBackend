package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidFieldDto {

    private UUID id;

    private String name;

    private String valueType;

    private UUID businessId;

    private Boolean tanlangan;
}
