package uz.dizgo.erp.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.dizgo.erp.entity.ExchangeProduct;
import uz.dizgo.erp.payload.ExchangeProductDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeProductMapper {
    @Mapping(source = "product.measurement.name", target = "measurementProductName")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(target = "productExchangeId", source = "product.id")
    ExchangeProductDTO toDto(ExchangeProduct exchangeProduct);

    List<ExchangeProductDTO> toDtoList(List<ExchangeProduct> exchangeProducts);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product.id", source = "productExchangeId")
    ExchangeProduct toEntity(ExchangeProductDTO exchangeProductDTO);

    @InheritInverseConfiguration
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product.id", source = "productExchangeId")
    void update(ExchangeProductDTO exchangeProductDTO, @MappingTarget ExchangeProduct exchangeProduct);

    void update(List<ExchangeProductDTO> exchangeProductDTOList, @MappingTarget List<ExchangeProduct> exchangeProductList);

}
