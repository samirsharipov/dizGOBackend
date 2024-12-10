package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.ExchangeProduct;
import uz.pdp.springsecurity.entity.Measurement;
import uz.pdp.springsecurity.entity.Product;
import uz.pdp.springsecurity.payload.ExchangeProductDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T12:12:11+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ExchangeProductMapperImpl implements ExchangeProductMapper {

    @Override
    public ExchangeProductDTO toDto(ExchangeProduct exchangeProduct) {
        if ( exchangeProduct == null ) {
            return null;
        }

        ExchangeProductDTO exchangeProductDTO = new ExchangeProductDTO();

        exchangeProductDTO.setMeasurementProductName( exchangeProductProductMeasurementName( exchangeProduct ) );
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
        exchangeProduct.setExchangeProductQuantity( exchangeProductDTO.getExchangeProductQuantity() );

        return exchangeProduct;
    }

    @Override
    public void update(ExchangeProductDTO exchangeProductDTO, ExchangeProduct exchangeProduct) {
        if ( exchangeProductDTO == null ) {
            return;
        }

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
}
