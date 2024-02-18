package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.entity.Payment;
import uz.pdp.springsecurity.payload.CustomerDto;
import uz.pdp.springsecurity.payload.PaymentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentMethodId", source = "payMethod.id")
    PaymentDto toDto(Payment payment);

    List<PaymentDto> toDtoList(List<Payment> paymentList);
}
