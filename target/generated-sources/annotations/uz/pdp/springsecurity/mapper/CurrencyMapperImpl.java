package uz.pdp.springsecurity.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.payload.CurrencyDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-27T16:17:53+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CurrencyMapperImpl implements CurrencyMapper {

    @Override
    public CurrencyDto toDto(Currency currency) {
        if ( currency == null ) {
            return null;
        }

        CurrencyDto currencyDto = new CurrencyDto();

        currencyDto.setCourse( currency.getCourse() );

        return currencyDto;
    }
}
