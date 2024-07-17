package uz.pdp.springsecurity.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.entity.Waiting;
import uz.pdp.springsecurity.payload.WaitingGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T11:50:50+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class WaitingMapperImpl implements WaitingMapper {

    @Override
    public WaitingGetDto toGetDto(Waiting waiting) {
        if ( waiting == null ) {
            return null;
        }

        WaitingGetDto waitingGetDto = new WaitingGetDto();

        waitingGetDto.setUserId( waitingUserId( waiting ) );
        waitingGetDto.setCustomerId( waitingCustomerId( waiting ) );
        waitingGetDto.setId( waiting.getId() );
        waitingGetDto.setDollar( waiting.getDollar() );
        waitingGetDto.setGross( waiting.getGross() );
        waitingGetDto.setTotalSum( waiting.getTotalSum() );
        waitingGetDto.setQuantity( waiting.getQuantity() );
        waitingGetDto.setDescription( waiting.getDescription() );

        return waitingGetDto;
    }

    private UUID waitingUserId(Waiting waiting) {
        if ( waiting == null ) {
            return null;
        }
        User user = waiting.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID waitingCustomerId(Waiting waiting) {
        if ( waiting == null ) {
            return null;
        }
        Customer customer = waiting.getCustomer();
        if ( customer == null ) {
            return null;
        }
        UUID id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
