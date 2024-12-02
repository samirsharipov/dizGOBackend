package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Business;
import uz.pdp.springsecurity.entity.Subscription;
import uz.pdp.springsecurity.entity.Tariff;
import uz.pdp.springsecurity.enums.PayType;
import uz.pdp.springsecurity.enums.StatusTariff;
import uz.pdp.springsecurity.payload.SubscriptionGetDto;
import uz.pdp.springsecurity.payload.SubscriptionPostDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T16:18:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public Subscription toEntity(SubscriptionPostDto subscriptionPostDto) {
        if ( subscriptionPostDto == null ) {
            return null;
        }

        Subscription subscription = new Subscription();

        subscription.setTariff( subscriptionPostDtoToTariff( subscriptionPostDto ) );
        subscription.setBusiness( subscriptionPostDtoToBusiness( subscriptionPostDto ) );
        if ( subscriptionPostDto.getPayType() != null ) {
            subscription.setPayType( Enum.valueOf( PayType.class, subscriptionPostDto.getPayType() ) );
        }
        subscription.setActiveNewTariff( subscriptionPostDto.isActiveNewTariff() );
        subscription.setCheckTestDay( subscriptionPostDto.isCheckTestDay() );

        subscription.setStatusTariff( StatusTariff.WAITING );

        return subscription;
    }

    @Override
    public SubscriptionGetDto toDto(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }

        SubscriptionGetDto subscriptionGetDto = new SubscriptionGetDto();

        subscriptionGetDto.setBusinessName( subscriptionBusinessName( subscription ) );
        subscriptionGetDto.setTariffName( subscriptionTariffName( subscription ) );
        subscriptionGetDto.setTariffPrice( subscriptionTariffPrice( subscription ) );
        subscriptionGetDto.setActive( subscription.isActive() );
        subscriptionGetDto.setId( subscription.getId() );
        if ( subscription.getStatusTariff() != null ) {
            subscriptionGetDto.setStatusTariff( subscription.getStatusTariff().name() );
        }
        subscriptionGetDto.setStartDay( subscription.getStartDay() );
        subscriptionGetDto.setEndDay( subscription.getEndDay() );
        if ( subscription.getPayType() != null ) {
            subscriptionGetDto.setPayType( subscription.getPayType().name() );
        }

        return subscriptionGetDto;
    }

    @Override
    public List<SubscriptionGetDto> toDtoList(List<Subscription> subscriptionList) {
        if ( subscriptionList == null ) {
            return null;
        }

        List<SubscriptionGetDto> list = new ArrayList<SubscriptionGetDto>( subscriptionList.size() );
        for ( Subscription subscription : subscriptionList ) {
            list.add( toDto( subscription ) );
        }

        return list;
    }

    protected Tariff subscriptionPostDtoToTariff(SubscriptionPostDto subscriptionPostDto) {
        if ( subscriptionPostDto == null ) {
            return null;
        }

        Tariff tariff = new Tariff();

        tariff.setId( subscriptionPostDto.getTariffId() );

        return tariff;
    }

    protected Business subscriptionPostDtoToBusiness(SubscriptionPostDto subscriptionPostDto) {
        if ( subscriptionPostDto == null ) {
            return null;
        }

        Business business = new Business();

        business.setId( subscriptionPostDto.getBusinessId() );

        return business;
    }

    private String subscriptionBusinessName(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }
        Business business = subscription.getBusiness();
        if ( business == null ) {
            return null;
        }
        String name = business.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String subscriptionTariffName(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }
        Tariff tariff = subscription.getTariff();
        if ( tariff == null ) {
            return null;
        }
        String name = tariff.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private double subscriptionTariffPrice(Subscription subscription) {
        if ( subscription == null ) {
            return 0.0d;
        }
        Tariff tariff = subscription.getTariff();
        if ( tariff == null ) {
            return 0.0d;
        }
        double price = tariff.getPrice();
        return price;
    }
}
