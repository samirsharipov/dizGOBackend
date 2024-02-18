package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.Tariff;
import uz.pdp.springsecurity.payload.CustomerDto;
import uz.pdp.springsecurity.payload.TariffDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TariffMapper {

    @Mapping(target = "permissionsList", ignore = true)
    @Mapping(target = "id",source = "id")
    TariffDto toDto(Tariff tariff);
    List<TariffDto> toDtoList(List<Tariff> tariffList);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Tariff toEntity(TariffDto tariffDto);

    @InheritInverseConfiguration
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void update(TariffDto tariffDto, @MappingTarget Tariff tariff);
}
