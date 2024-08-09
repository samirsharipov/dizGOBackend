package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.ExchangeProduct;
import uz.pdp.springsecurity.entity.Measurement;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.entity.ProductTypePrice;
import uz.pdp.springsecurity.payload.ExchangeProductDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-09T15:35:04+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class ExchangeProductMapperImpl implements ExchangeProductMapper {

    @Override
    public ExchangeProductDTO toDto(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }

        ExchangeProductDTO exchangeProductDTO = new ExchangeProductDTO();

        exchangeProductDTO.setMeasurementProductTypePriceName( exchangeProductProductTypePriceProductMeasurementName( exchangeProduct ) );
        exchangeProductDTO.setMeasurementProductName( exchangeProductProductMeasurementName( exchangeProduct ) );
        exchangeProductDTO.setProductTypePriceName( exchangeProductProductTypePriceProductName( exchangeProduct ) );
        exchangeProductDTO.setProductName( exchangeProductProductName( exchangeProduct ) );
        exchangeProductDTO.setProductExchangeId( exchangeProductProductId( exchangeProduct ) );
        exchangeProductDTO.setExchangeProductQuantity( exchangeProduct.getExchangeProductQuantity() );

        return exchangeProductDTO;
    }

    @Override
    public List<ExchangeProductDTO> toDtoList(List<ExchangeProduct> exchangeProducts) {
        if ( exchangeProducts == null ) {
            return null;
        }

        List<ExchangeProductDTO> list = new ArrayList<ExchangeProductDTO>( exchangeProducts.size() );
        for ( ExchangeProduct exchangeProduct : exchangeProducts ) {
            list.add( toDto( exchangeProduct ) );
        }

        return list;
    }

    @Override
    public ExchangeProduct toEntity(ExchangeProductDTO exchangeProductDTO) {
        if ( exchangeProductDTO == null ) {
            return null;
        }

        ExchangeProduct exchangeProduct = new ExchangeProduct();

        exchangeProduct.setProduct( exchangeProductDTOToProduct( exchangeProductDTO ) );
        exchangeProduct.setProductTypePrice( exchangeProductDTOToProductTypePrice( exchangeProductDTO ) );
        exchangeProduct.setExchangeProductQuantity( exchangeProductDTO.getExchangeProductQuantity() );

        return exchangeProduct;
    }

    @Override
    public void update(ExchangeProductDTO exchangeProductDTO, ExchangeProduct exchangeProduct) {
        if ( exchangeProductDTO == null ) {
            return;
        }

        if ( exchangeProduct.getProductTypePrice() == null ) {
            exchangeProduct.setProductTypePrice( new ProductTypePrice() );
        }
        exchangeProductDTOToProductTypePrice1( exchangeProductDTO, exchangeProduct.getProductTypePrice() );
        exchangeProduct.setExchangeProductQuantity( exchangeProductDTO.getExchangeProductQuantity() );
    }

    @Override
    public void update(List<ExchangeProductDTO> exchangeProductDTOList, List<ExchangeProduct> exchangeProductList) {
        if ( exchangeProductDTOList == null ) {
            return;
        }

        exchangeProductList.clear();
        for ( ExchangeProductDTO exchangeProductDTO : exchangeProductDTOList ) {
            exchangeProductList.add( toEntity( exchangeProductDTO ) );
        }
    }

    private String exchangeProductProductTypePriceProductMeasurementName(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }
        ProductTypePrice productTypePrice = exchangeProduct.getProductTypePrice();
        if ( productTypePrice == null ) {
            return null;
        }
        Product product = productTypePrice.getProduct();
        if ( product == null ) {
            return null;
        }
        Measurement measurement = product.getMeasurement();
        if ( measurement == null ) {
            return null;
        }
        String name = measurement.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String exchangeProductProductMeasurementName(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }
        Product product = exchangeProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        Measurement measurement = product.getMeasurement();
        if ( measurement == null ) {
            return null;
        }
        String name = measurement.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String exchangeProductProductTypePriceProductName(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }
        ProductTypePrice productTypePrice = exchangeProduct.getProductTypePrice();
        if ( productTypePrice == null ) {
            return null;
        }
        Product product = productTypePrice.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String exchangeProductProductName(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }
        Product product = exchangeProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID exchangeProductProductId(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }
        Product product = exchangeProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        UUID id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Product exchangeProductDTOToProduct(ExchangeProductDTO exchangeProductDTO) {
        if ( exchangeProductDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( exchangeProductDTO.getProductExchangeId() );

        return product;
    }

    protected ProductTypePrice exchangeProductDTOToProductTypePrice(ExchangeProductDTO exchangeProductDTO) {
        if ( exchangeProductDTO == null ) {
            return null;
        }

        ProductTypePrice productTypePrice = new ProductTypePrice();

        productTypePrice.setId( exchangeProductDTO.getProductTypePriceId() );

        return productTypePrice;
    }

    protected void exchangeProductDTOToMeasurement(ExchangeProductDTO exchangeProductDTO, Measurement mappingTarget) {
        if ( exchangeProductDTO == null ) {
            return;
        }

        mappingTarget.setName( exchangeProductDTO.getMeasurementProductTypePriceName() );
    }

    protected void exchangeProductDTOToProduct1(ExchangeProductDTO exchangeProductDTO, Product mappingTarget) {
        if ( exchangeProductDTO == null ) {
            return;
        }

        if ( mappingTarget.getMeasurement() == null ) {
            mappingTarget.setMeasurement( new Measurement() );
        }
        exchangeProductDTOToMeasurement( exchangeProductDTO, mappingTarget.getMeasurement() );
        mappingTarget.setName( exchangeProductDTO.getProductTypePriceName() );
    }

    protected void exchangeProductDTOToProductTypePrice1(ExchangeProductDTO exchangeProductDTO, ProductTypePrice mappingTarget) {
        if ( exchangeProductDTO == null ) {
            return;
        }

        if ( mappingTarget.getProduct() == null ) {
            mappingTarget.setProduct( new Product() );
        }
        exchangeProductDTOToProduct1( exchangeProductDTO, mappingTarget.getProduct() );
        mappingTarget.setId( exchangeProductDTO.getProductTypePriceId() );
    }
}
