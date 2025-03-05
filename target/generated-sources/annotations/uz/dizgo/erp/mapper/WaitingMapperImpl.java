package uz.dizgo.erp.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Customer;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.entity.Waiting;
import uz.dizgo.erp.payload.WaitingGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
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
