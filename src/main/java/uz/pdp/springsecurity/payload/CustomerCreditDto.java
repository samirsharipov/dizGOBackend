package uz.pdp.springsecurity.payload;


import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CustomerCreditDto {
    private UUID customerId;
    private LocalDate paymentDate;
    private Double totalAmount;
    private int month;
    private String comment;
}
