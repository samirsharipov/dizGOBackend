package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Waiting;
import uz.dizgo.erp.payload.WaitingGetDto;

@Mapper(componentModel = "spring")
public interface WaitingMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "waitingProductGetDtoList", ignore = true)
    WaitingGetDto toGetDto(Waiting waiting);
}
