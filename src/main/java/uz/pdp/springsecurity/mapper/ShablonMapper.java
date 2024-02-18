package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.Shablon;
import uz.pdp.springsecurity.payload.ShablonDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShablonMapper {
    @Mapping(source = "businessId", target = "business.id")
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "originalName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Shablon toEntity(ShablonDto shablonDto);

    @Mapping(source = "business.id", target = "businessId")
    ShablonDto toDto(Shablon shablon);

    List<ShablonDto> toDto(List<Shablon> shablonList);

    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "originalName", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void update(ShablonDto shablonDto, @MappingTarget Shablon shablon);
}
