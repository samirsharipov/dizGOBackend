package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitAndLost {
    private double totalBuySum;
    private double totalSaleSum;
    private double otherExpenses;
    private double benefit;
    private double lost;
    @ManyToOne
    private Branch branch;
}
