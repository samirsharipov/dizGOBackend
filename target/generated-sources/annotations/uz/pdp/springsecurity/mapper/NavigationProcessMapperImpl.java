package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.NavigationProcess;
import uz.pdp.springsecurity.payload.NavigationProcessDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-01T15:47:00+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class NavigationProcessMapperImpl implements NavigationProcessMapper {

    @Override
    public NavigationProcessDto toDto(NavigationProcess navigationProcess) {
        if ( navigationProcess == null ) {
            return null;
        }

        NavigationProcessDto navigationProcessDto = new NavigationProcessDto();

        navigationProcessDto.setReal( navigationProcess.isReal() );
        navigationProcessDto.setDate( navigationProcess.getDate() );
        navigationProcessDto.setTotalSell( navigationProcess.getTotalSell() );
        navigationProcessDto.setSeller( navigationProcess.getSeller() );
        navigationProcessDto.setAverageSell( navigationProcess.getAverageSell() );
        navigationProcessDto.setProduct( navigationProcess.getProduct() );
        navigationProcessDto.setProductBuyPrice( navigationProcess.getProductBuyPrice() );
        navigationProcessDto.setProductSalePrice( navigationProcess.getProductSalePrice() );
        navigationProcessDto.setProducedProductAmount( navigationProcess.getProducedProductAmount() );
        navigationProcessDto.setProducedProductPrice( navigationProcess.getProducedProductPrice() );
        navigationProcessDto.setCustomer( navigationProcess.getCustomer() );
        navigationProcessDto.setLid( navigationProcess.getLid() );
        navigationProcessDto.setLidPrice( navigationProcess.getLidPrice() );
        navigationProcessDto.setSalary( navigationProcess.getSalary() );
        navigationProcessDto.setTotalUser( navigationProcess.getTotalUser() );
        navigationProcessDto.setOutlay( navigationProcess.getOutlay() );

        return navigationProcessDto;
    }

    @Override
    public List<NavigationProcessDto> toDtoList(List<NavigationProcess> navigationProcessList) {
        if ( navigationProcessList == null ) {
            return null;
        }

        List<NavigationProcessDto> list = new ArrayList<NavigationProcessDto>( navigationProcessList.size() );
        for ( NavigationProcess navigationProcess : navigationProcessList ) {
            list.add( toDto( navigationProcess ) );
        }

        return list;
    }
}
