package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendOutlay {
    private UUID id;
    private String name;
    private String branch;
    private String payment;
}
