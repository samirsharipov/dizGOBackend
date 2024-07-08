package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Tariff;
import uz.pdp.springsecurity.enums.Lifetime;
import uz.pdp.springsecurity.payload.TariffDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T11:50:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class TariffMapperImpl implements TariffMapper {

    @Override
    public TariffDto toDto(Tariff tariff) {
        if ( tariff == null ) {
            return null;
        }

        TariffDto tariffDto = new TariffDto();

        tariffDto.setId( tariff.getId() );
        tariffDto.setName( tariff.getName() );
        tariffDto.setDescription( tariff.getDescription() );
        tariffDto.setBranchAmount( tariff.getBranchAmount() );
        tariffDto.setProductAmount( tariff.getProductAmount() );
        tariffDto.setEmployeeAmount( tariff.getEmployeeAmount() );
        tariffDto.setTradeAmount( tariff.getTradeAmount() );
        if ( tariff.getLifetime() != null ) {
            tariffDto.setLifetime( tariff.getLifetime().name() );
        }
        tariffDto.setTestDay( tariff.getTestDay() );
        tariffDto.setInterval( tariff.getInterval() );
        tariffDto.setPrice( tariff.getPrice() );
        tariffDto.setDiscount( tariff.getDiscount() );
        tariffDto.setActive( tariff.isActive() );
        tariffDto.setDelete( tariff.isDelete() );

        return tariffDto;
    }

    @Override
    public List<TariffDto> toDtoList(List<Tariff> tariffList) {
        if ( tariffList == null ) {
            return null;
        }

        List<TariffDto> list = new ArrayList<TariffDto>( tariffList.size() );
        for ( Tariff tariff : tariffList ) {
            list.add( toDto( tariff ) );
        }

        return list;
    }

    @Override
    public Tariff toEntity(TariffDto tariffDto) {
        if ( tariffDto == null ) {
            return null;
        }

        Tariff tariff = new Tariff();

        tariff.setName( tariffDto.getName() );
        tariff.setDescription( tariffDto.getDescription() );
        tariff.setBranchAmount( tariffDto.getBranchAmount() );
        tariff.setProductAmount( tariffDto.getProductAmount() );
        tariff.setEmployeeAmount( tariffDto.getEmployeeAmount() );
        tariff.setTradeAmount( tariffDto.getTradeAmount() );
        if ( tariffDto.getLifetime() != null ) {
            tariff.setLifetime( Enum.valueOf( Lifetime.class, tariffDto.getLifetime() ) );
        }
        tariff.setTestDay( tariffDto.getTestDay() );
        tariff.setInterval( tariffDto.getInterval() );
        tariff.setPrice( tariffDto.getPrice() );
        tariff.setDiscount( tariffDto.getDiscount() );
        tariff.setActive( tariffDto.isActive() );
        tariff.setDelete( tariffDto.isDelete() );

        return tariff;
    }

    @Override
    public void update(TariffDto tariffDto, Tariff tariff) {
        if ( tariffDto == null ) {
            return;
        }

        tariff.setName( tariffDto.getName() );
        tariff.setDescription( tariffDto.getDescription() );
        tariff.setBranchAmount( tariffDto.getBranchAmount() );
        tariff.setProductAmount( tariffDto.getProductAmount() );
        tariff.setEmployeeAmount( tariffDto.getEmployeeAmount() );
        tariff.setTradeAmount( tariffDto.getTradeAmount() );
        if ( tariffDto.getLifetime() != null ) {
            tariff.setLifetime( Enum.valueOf( Lifetime.class, tariffDto.getLifetime() ) );
        }
        else {
            tariff.setLifetime( null );
        }
        tariff.setTestDay( tariffDto.getTestDay() );
        tariff.setInterval( tariffDto.getInterval() );
        tariff.setPrice( tariffDto.getPrice() );
        tariff.setDiscount( tariffDto.getDiscount() );
        tariff.setActive( tariffDto.isActive() );
        tariff.setDelete( tariffDto.isDelete() );
    }
}
