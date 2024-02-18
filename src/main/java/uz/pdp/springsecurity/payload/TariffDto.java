package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Permissions;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    private UUID id;

    private String name;

    private String description;

    private int branchAmount;

    private long productAmount;

    private int employeeAmount;

    private long tradeAmount;

    private String lifetime;

    private int testDay;

    private int interval;

    private double price;

    private double discount;

    private boolean isActive;

    private boolean isDelete;

    private List<Permissions> permissionsList;
}
