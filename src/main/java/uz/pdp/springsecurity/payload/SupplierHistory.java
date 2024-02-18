package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierHistory {
    private UUID id;
    private String payment;
    private Double amount;
    private String branch;
    private String user;
    private String supplier;
    private Date date;
    private String description;
}
