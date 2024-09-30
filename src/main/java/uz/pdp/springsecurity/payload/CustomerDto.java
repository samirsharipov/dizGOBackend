package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private UUID id;

    private String name;

    private String phoneNumber;

    private String telegram;

    private String description;

    private UUID customerGroupId;

    private String customerGroupName;

    private double customerGroupPercent;

    /*private UUID businessId;

    private UUID branchId;*/

    @NotNull(message = "required line")
    private List<UUID> branches;

    private double debt;

    private Date payDate;



    private Date birthday;
    private Double latitude;   // Yangi maydon
    private Double longitude;  // Yangi maydon
    private String address;  // Yangi qo'shilgan maydon


    @NotNull(message = "required line")
    private Boolean lidCustomer;

}
