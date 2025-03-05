package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import uz.dizgo.erp.entity.Currency;
import uz.dizgo.erp.payload.CurrencyDto;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency currency);
}
