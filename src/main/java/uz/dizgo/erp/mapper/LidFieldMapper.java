package uz.dizgo.erp.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.LidField;
import uz.dizgo.erp.payload.LidFieldDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LidFieldMapper {

    @Mapping(target = "businessId", source = "business.id")
    @Mapping(target = "id", source = "id")
    LidFieldDto toDto(LidField lidField);

    List<LidFieldDto> toDto(List<LidField> lidFields);


    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    LidField toEntity(LidFieldDto lidFieldDto);

    List<LidField> toEntity(List<LidFieldDto> lidFieldDtoList);

    @InheritInverseConfiguration
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    void update(LidFieldDto lidFieldDto, @MappingTarget LidField lidField);
}
