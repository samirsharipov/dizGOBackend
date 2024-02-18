package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Waiting;
import uz.pdp.springsecurity.payload.WaitingGetDto;

@Mapper(componentModel = "spring")
public interface WaitingMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "waitingProductGetDtoList", ignore = true)
    WaitingGetDto toGetDto(Waiting waiting);
}
