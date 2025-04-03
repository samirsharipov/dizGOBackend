package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Trade;
import uz.dizgo.erp.payload.TradeLidDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TradeLidMapper {
    @Mapping(target = "customerPhoneNumber", source = "customer.phoneNumber")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(source = "trader.firstName", target = "traderName")
    @Mapping(source = "createdAt", target = "timestamp")
    @Mapping(source = "paymentStatus.status", target = "paymentStatusName")
    @Mapping(source = "payMethod.type", target = "paymentMethodName")
    @Mapping(source = "branch.name", target = "branchName")
    TradeLidDto toDto(Trade trade);

    List<TradeLidDto> toDto(List<Trade> tradeList);
}
