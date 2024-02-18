package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.SelectForLid;
import uz.pdp.springsecurity.payload.SelectForLidDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SelectForLidMapper {
    @Mapping(target = "lidField", ignore = true)
    @Mapping(source = "lidFieldId", target = "lidField.id")
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SelectForLid toEntity(SelectForLidDto selectForLidDto);

    List<SelectForLid> toEntity(List<SelectForLidDto> selectForLids);

    @Mapping(source = "lidField.id", target = "lidFieldId")
    SelectForLidDto toDto(SelectForLid selectForLid);

    List<SelectForLidDto> toDto(List<SelectForLid> selectForLids);

    @InheritInverseConfiguration
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "lidFieldId", target = "lidField.id")
    void update(SelectForLidDto selectForLidDto, @MappingTarget SelectForLid selectForLid);
}
