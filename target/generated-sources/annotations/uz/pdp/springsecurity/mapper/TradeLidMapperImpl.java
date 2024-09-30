package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.entity.PaymentStatus;
import uz.pdp.springsecurity.entity.Trade;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.TradeLidDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-30T00:25:19+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class TradeLidMapperImpl implements TradeLidMapper {

    @Override
    public TradeLidDto toDto(Trade trade) {
        if ( trade == null ) {
            return null;
        }

        TradeLidDto tradeLidDto = new TradeLidDto();

        tradeLidDto.setTraderName( tradeTraderFirstName( trade ) );
        tradeLidDto.setTimestamp( trade.getCreatedAt() );
        tradeLidDto.setPaymentStatusName( tradePaymentStatusStatus( trade ) );
        tradeLidDto.setPaymentMethodName( tradePayMethodType( trade ) );
        tradeLidDto.setCustomerPhoneNumber( tradeCustomerPhoneNumber( trade ) );
        tradeLidDto.setCustomerName( tradeCustomerName( trade ) );
        tradeLidDto.setBranchName( tradeBranchName( trade ) );
        tradeLidDto.setId( trade.getId() );
        tradeLidDto.setTotalSum( trade.getTotalSum() );
        tradeLidDto.setPaidSum( trade.getPaidSum() );
        tradeLidDto.setDebtSum( trade.getDebtSum() );

        return tradeLidDto;
    }

    @Override
    public List<TradeLidDto> toDto(List<Trade> tradeList) {
        if ( tradeList == null ) {
            return null;
        }

        List<TradeLidDto> list = new ArrayList<TradeLidDto>( tradeList.size() );
        for ( Trade trade : tradeList ) {
            list.add( toDto( trade ) );
        }

        return list;
    }

    private String tradeTraderFirstName(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        User trader = trade.getTrader();
        if ( trader == null ) {
            return null;
        }
        String firstName = trader.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String tradePaymentStatusStatus(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        PaymentStatus paymentStatus = trade.getPaymentStatus();
        if ( paymentStatus == null ) {
            return null;
        }
        String status = paymentStatus.getStatus();
        if ( status == null ) {
            return null;
        }
        return status;
    }

    private String tradePayMethodType(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        PaymentMethod payMethod = trade.getPayMethod();
        if ( payMethod == null ) {
            return null;
        }
        String type = payMethod.getType();
        if ( type == null ) {
            return null;
        }
        return type;
    }

    private String tradeCustomerPhoneNumber(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        Customer customer = trade.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String phoneNumber = customer.getPhoneNumber();
        if ( phoneNumber == null ) {
            return null;
        }
        return phoneNumber;
    }

    private String tradeCustomerName(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        Customer customer = trade.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String name = customer.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String tradeBranchName(Trade trade) {
        if ( trade == null ) {
            return null;
        }
        Branch branch = trade.getBranch();
        if ( branch == null ) {
            return null;
        }
        String name = branch.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
