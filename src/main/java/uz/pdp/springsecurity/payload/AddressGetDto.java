package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressGetDto {
    private UUID id; // Address ID
    private String name; // Address nomi
    private UUID parentId; // Ota address IDsi
    private String parentName; // Ota address nomi
    private List<AddressGetDto> children; // Bolalar ro'yxati

    public AddressGetDto(UUID id, String name, UUID parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }
}