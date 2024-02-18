package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminInfoDto {
    private int subscribers;
    private int rejected;
    private int waiting;
    private double subscriptionPayment;
}
