package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.ExchangeProduct;
import uz.pdp.springsecurity.payload.ExchangeProductDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeProductMapper {
    @Mapping(source = "productTypePrice.product.measurement.name", target = "measurementProductTypePriceName")
    @Mapping(source = "product.measurement.name", target = "measurementProductName")
    @Mapping(source = "productTypePrice.product.name", target = "productTypePriceName")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(target = "productTypePriceId", ignore = true)
    @Mapping(target = "productExchangeId", source = "product.id")
    ExchangeProductDTO toDto(ExchangeProduct exchangeProduct);

    List<ExchangeProductDTO> toDtoList(List<ExchangeProduct> exchangeProducts);

    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product.id", source = "productExchangeId")
    @Mapping(target = "productTypePrice.id", source = "productTypePriceId")
    ExchangeProduct toEntity(ExchangeProductDTO exchangeProductDTO);

    @InheritInverseConfiguration
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product.id", source = "productExchangeId")
    @Mapping(target = "productTypePrice.id", source = "productTypePriceId")
    void update(ExchangeProductDTO exchangeProductDTO, @MappingTarget ExchangeProduct exchangeProduct);

    void update(List<ExchangeProductDTO> exchangeProductDTOList, @MappingTarget List<ExchangeProduct> exchangeProductList);

}
