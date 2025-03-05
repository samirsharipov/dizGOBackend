package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private UUID id; // Location ID
    private UUID branchId; // Filial ID
    private double latitude; // Kenglik
    private double longitude; // Uzunlik
    private double radius; // Radius
}