package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Balance;
import uz.pdp.springsecurity.entity.BalanceHistory;
import uz.pdp.springsecurity.payload.BalanceHistoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    @Mapping(source = "balance.paymentMethod.type", target = "payMethodType")
    @Mapping(source = "balance.paymentMethod.id", target = "payMethodId")
    @Mapping(source = "balance.id", target = "balanceId")
    @Mapping(source = "createdAt", target = "date")
    BalanceHistoryDto toDto(BalanceHistory balanceHistory);

    List<BalanceHistoryDto> toDto(List<BalanceHistory> balanceHistoryList);
}
