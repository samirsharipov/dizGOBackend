package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Payment;
import uz.dizgo.erp.payload.PaymentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "isDollar", ignore = true)
    @Mapping(target = "paymentMethodId", source = "payMethod.id")
    PaymentDto toDto(Payment payment);

    List<PaymentDto> toDtoList(List<Payment> paymentList);
}
