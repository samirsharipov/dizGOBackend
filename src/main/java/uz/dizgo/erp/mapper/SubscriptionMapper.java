package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Subscription;
import uz.dizgo.erp.payload.SubscriptionGetDto;
import uz.dizgo.erp.payload.SubscriptionPostDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "testDayFinish", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "tariff", ignore = true)
    @Mapping(target = "startDay", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endDay", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "tariff.id", source = "tariffId")
    @Mapping(target = "business.id", source = "businessId")
    @Mapping(target = "statusTariff", constant = "WAITING")
    Subscription toEntity(SubscriptionPostDto subscriptionPostDto);

    @Mapping(target = "totalSum", ignore = true)
    @Mapping(target = "businessName", source = "business.name")
    @Mapping(target = "tariffName", source = "tariff.name")
    @Mapping(target = "tariffPrice", source = "tariff.price")
    @Mapping(target = "active", source = "active")
    SubscriptionGetDto toDto(Subscription subscription);

    List<SubscriptionGetDto> toDtoList(List<Subscription> subscriptionList);
}
