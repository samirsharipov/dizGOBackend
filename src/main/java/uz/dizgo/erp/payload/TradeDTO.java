package uz.dizgo.erp.payload;

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

public class TradeDTO {
    @NotNull
    private boolean backing;

    private UUID customerId;
    private String customerName;

    @NotNull
    private UUID userId;
    private String userName;

    @NotNull
    private UUID branchId;
    private String branchName;

    private UUID paymentStatusId;
    private String paymentStatusName;

    @NotNull
    private List<PaymentDto> paymentDtoList;

    @NotNull
    private Date payDate;

    @NotNull
    private double totalSum;

    @NotNull
    private double paidSum;
    @NotNull
    private double paidSumDollar;

    @NotNull
    private double debtSum;

    @NotNull
    private List<TradeProductDto> productTraderDto;

    private boolean lid;

    private String dollar;

    private String gross;
    private boolean dollarTrade;
    private Double dollarPrice;
    private Integer debdSum;
    private Boolean isSuccess;
    private Boolean firstPaymentIsDollar;
    private String tradeDescription;
    private Boolean differentPayment;

    private CustomerCreditDto customerCreditDto;
}