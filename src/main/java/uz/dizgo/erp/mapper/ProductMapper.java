package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.payload.ProductGetForPurchaseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "productTypePriceId", ignore = true)
    ProductGetForPurchaseDto toProductGetForPurchaseDto(Product product);

    List<ProductGetForPurchaseDto> toProductGetForPurchaseDtoList(List<Product> products);
}
