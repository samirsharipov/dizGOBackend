package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Balance;
import uz.pdp.springsecurity.entity.BalanceHistory;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.payload.BalanceHistoryDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-01T16:39:34+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class BalanceMapperImpl implements BalanceMapper {

    @Override
    public BalanceHistoryDto toDto(BalanceHistory balanceHistory) {
        if ( balanceHistory == null ) {
            return null;
        }

        BalanceHistoryDto balanceHistoryDto = new BalanceHistoryDto();

        balanceHistoryDto.setPayMethodType( balanceHistoryBalancePaymentMethodType( balanceHistory ) );
        balanceHistoryDto.setPayMethodId( balanceHistoryBalancePaymentMethodId( balanceHistory ) );
        balanceHistoryDto.setBalanceId( balanceHistoryBalanceId( balanceHistory ) );
        balanceHistoryDto.setDate( balanceHistory.getCreatedAt() );
        balanceHistoryDto.setSumma( balanceHistory.getSumma() );
        balanceHistoryDto.setPlus( balanceHistory.isPlus() );
        balanceHistoryDto.setAccountSumma( balanceHistory.getAccountSumma() );
        balanceHistoryDto.setTotalSumma( balanceHistory.getTotalSumma() );
        balanceHistoryDto.setCurrency( balanceHistory.getCurrency() );
        balanceHistoryDto.setDescription( balanceHistory.getDescription() );

        return balanceHistoryDto;
    }

    @Override
    public List<BalanceHistoryDto> toDto(List<BalanceHistory> balanceHistoryList) {
        if ( balanceHistoryList == null ) {
            return null;
        }

        List<BalanceHistoryDto> list = new ArrayList<BalanceHistoryDto>( balanceHistoryList.size() );
        for ( BalanceHistory balanceHistory : balanceHistoryList ) {
            list.add( toDto( balanceHistory ) );
        }

        return list;
    }

    private String balanceHistoryBalancePaymentMethodType(BalanceHistory balanceHistory) {
        if ( balanceHistory == null ) {
            return null;
        }
        Balance balance = balanceHistory.getBalance();
        if ( balance == null ) {
            return null;
        }
        PaymentMethod paymentMethod = balance.getPaymentMethod();
        if ( paymentMethod == null ) {
            return null;
        }
        String type = paymentMethod.getType();
        if ( type == null ) {
            return null;
        }
        return type;
    }

    private UUID balanceHistoryBalancePaymentMethodId(BalanceHistory balanceHistory) {
        if ( balanceHistory == null ) {
            return null;
        }
        Balance balance = balanceHistory.getBalance();
        if ( balance == null ) {
            return null;
        }
        PaymentMethod paymentMethod = balance.getPaymentMethod();
        if ( paymentMethod == null ) {
            return null;
        }
        UUID id = paymentMethod.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID balanceHistoryBalanceId(BalanceHistory balanceHistory) {
        if ( balanceHistory == null ) {
            return null;
        }
        Balance balance = balanceHistory.getBalance();
        if ( balance == null ) {
            return null;
        }
        UUID id = balance.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
