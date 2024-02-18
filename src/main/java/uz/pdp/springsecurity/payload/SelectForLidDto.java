package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectForLidDto {
    private UUID id;
    private String name;
    private UUID lidFieldId;

    public SelectForLidDto(String name, UUID lidFieldId) {
        this.name = name;
        this.lidFieldId = lidFieldId;
    }
}
