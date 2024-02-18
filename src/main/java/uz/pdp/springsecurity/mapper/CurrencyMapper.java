package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.Currency;
import uz.pdp.springsecurity.payload.CurrencyDto;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency currency);
}
