package uz.dizgo.erp.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Category;
import uz.dizgo.erp.entity.Product;
import uz.dizgo.erp.payload.ProductGetForPurchaseDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductGetForPurchaseDto toProductGetForPurchaseDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductGetForPurchaseDto productGetForPurchaseDto = new ProductGetForPurchaseDto();

        productGetForPurchaseDto.setProductId( product.getId() );
        productGetForPurchaseDto.setCategoryId( productCategoryId( product ) );
        productGetForPurchaseDto.setName( product.getName() );
        productGetForPurchaseDto.setBarcode( product.getBarcode() );
        productGetForPurchaseDto.setBuyPrice( product.getBuyPrice() );
        productGetForPurchaseDto.setSalePrice( product.getSalePrice() );
        productGetForPurchaseDto.setBuyPriceDollar( product.getBuyPriceDollar() );
        productGetForPurchaseDto.setBuyDollar( product.isBuyDollar() );
        productGetForPurchaseDto.setSalePriceDollar( product.getSalePriceDollar() );
        productGetForPurchaseDto.setSaleDollar( product.isSaleDollar() );
        productGetForPurchaseDto.setGrossPrice( product.getGrossPrice() );
        productGetForPurchaseDto.setGrossPriceDollar( product.getGrossPriceDollar() );
        productGetForPurchaseDto.setGrossPriceMyControl( product.getGrossPriceMyControl() );
        productGetForPurchaseDto.setProfitPercent( product.getProfitPercent() );
        productGetForPurchaseDto.setMinQuantity( product.getMinQuantity() );

        return productGetForPurchaseDto;
    }

    @Override
    public List<ProductGetForPurchaseDto> toProductGetForPurchaseDtoList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductGetForPurchaseDto> list = new ArrayList<ProductGetForPurchaseDto>( products.size() );
        for ( Product product : products ) {
            list.add( toProductGetForPurchaseDto( product ) );
        }

        return list;
    }

    private UUID productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        UUID id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
