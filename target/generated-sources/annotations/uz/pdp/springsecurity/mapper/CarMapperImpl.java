package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Car;
import uz.pdp.springsecurity.payload.CarDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-28T11:55:46+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public Car toEntity(CarDto carDto) {
        if ( carDto == null ) {
            return null;
        }

        Car car = new Car();

        car.setBusiness( carDtoToBusiness( carDto ) );
        car.setDriver( carDto.getDriver() );
        car.setModel( carDto.getModel() );
        car.setColor( carDto.getColor() );
        car.setCarNumber( carDto.getCarNumber() );
        car.setPrice( carDto.getPrice() );
        car.setFile( map( carDto.getFile() ) );

        return car;
    }

    @Override
    public CarDto toDto(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDto carDto = new CarDto();

        carDto.setBusinessId( carBusinessId( car ) );
        carDto.setId( car.getId() );
        carDto.setDriver( car.getDriver() );
        carDto.setModel( car.getModel() );
        carDto.setColor( car.getColor() );
        carDto.setCarNumber( car.getCarNumber() );
        carDto.setFile( map( car.getFile() ) );
        carDto.setPrice( car.getPrice() );

        return carDto;
    }

    @Override
    public List<CarDto> toDto(List<Car> cars) {
        if ( cars == null ) {
            return null;
        }

        List<CarDto> list = new ArrayList<CarDto>( cars.size() );
        for ( Car car : cars ) {
            list.add( toDto( car ) );
        }

        return list;
    }

    @Override
    public void update(CarDto carDto, Car car) {
        if ( carDto == null ) {
            return;
        }

        if ( car.getBusiness() == null ) {
            car.setBusiness( new Business() );
        }
        carDtoToBusiness1( carDto, car.getBusiness() );
        car.setDriver( carDto.getDriver() );
        car.setModel( carDto.getModel() );
        car.setColor( carDto.getColor() );
        car.setCarNumber( carDto.getCarNumber() );
        car.setPrice( carDto.getPrice() );
        car.setFile( map( carDto.getFile() ) );
    }

    protected Business carDtoToBusiness(CarDto carDto) {
        if ( carDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( carDto.getBusinessId() );

        return business;
    }

    private UUID carBusinessId(Car car) {
        if ( car == null ) {
            return null;
        }
        Business business = car.getBusiness();
        if ( business == null ) {
            return null;
        }
        UUID id = business.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void carDtoToBusiness1(CarDto carDto, Business mappingTarget) {
        if ( carDto == null ) {
            return;
        }

        mappingTarget.setId( carDto.getBusinessId() );
    }
}
