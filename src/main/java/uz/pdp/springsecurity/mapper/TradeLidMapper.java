package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Trade;
import uz.pdp.springsecurity.payload.TradeLidDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeLidMapper {
    @Mapping(source = "trader.firstName", target = "traderName")
    @Mapping(source = "createdAt", target = "timestamp")
    @Mapping(source = "paymentStatus.status", target = "paymentStatusName")
    @Mapping(source = "payMethod.type", target = "paymentMethodName")
    @Mapping(source = "customer.phoneNumber", target = "customerPhoneNumber")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "branch.name", target = "branchName")
    TradeLidDto toDto(Trade trade);

    List<TradeLidDto> toDto(List<Trade> tradeList);
}
