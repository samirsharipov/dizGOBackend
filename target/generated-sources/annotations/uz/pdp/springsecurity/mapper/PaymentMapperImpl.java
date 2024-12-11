package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Payment;
import uz.pdp.springsecurity.entity.PaymentMethod;
import uz.pdp.springsecurity.payload.PaymentDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T14:21:47+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setPaymentMethodId( paymentPayMethodId( payment ) );
        paymentDto.setPaidSum( payment.getPaidSum() );
        paymentDto.setPaidSumDollar( payment.getPaidSumDollar() );

        return paymentDto;
    }

    @Override
    public List<PaymentDto> toDtoList(List<Payment> paymentList) {
        if ( paymentList == null ) {
            return null;
        }

        List<PaymentDto> list = new ArrayList<PaymentDto>( paymentList.size() );
        for ( Payment payment : paymentList ) {
            list.add( toDto( payment ) );
        }

        return list;
    }

    private UUID paymentPayMethodId(Payment payment) {
        if ( payment == null ) {
            return null;
        }
        PaymentMethod payMethod = payment.getPayMethod();
        if ( payMethod == null ) {
            return null;
        }
        UUID id = payMethod.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
