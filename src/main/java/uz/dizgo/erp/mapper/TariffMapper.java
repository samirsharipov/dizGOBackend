package uz.dizgo.erp.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.Tariff;
import uz.dizgo.erp.payload.TariffDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TariffMapper {

    @Mapping(target = "id",source = "id")
    TariffDto toDto(Tariff tariff);

    List<TariffDto> toDtoList(List<Tariff> tariffList);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
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
