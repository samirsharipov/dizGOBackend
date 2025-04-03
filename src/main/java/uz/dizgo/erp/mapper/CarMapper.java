package uz.dizgo.erp.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.Car;
import uz.dizgo.erp.payload.CarDto;
import uz.dizgo.erp.entity.Attachment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(source = "businessId", target = "business.id")
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Car toEntity(CarDto carDto);

    @Mapping(source = "business.id", target = "businessId")
    CarDto toDto(Car car);

    List<CarDto> toDto(List<Car> cars);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    void update(CarDto carDto, @MappingTarget Car car);

    default Attachment map(String value) {
        if (value == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setFileOriginalName(value);
        // Set other properties of Attachment as needed
        return attachment;
    }

    default String map(Attachment value) {
        return (value != null) ? value.getFileOriginalName() : null;
    }
}
