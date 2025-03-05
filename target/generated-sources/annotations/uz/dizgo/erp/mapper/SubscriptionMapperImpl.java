package uz.dizgo.erp.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.dizgo.erp.entity.Business;
import uz.dizgo.erp.entity.Subscription;
import uz.dizgo.erp.entity.Tariff;
import uz.dizgo.erp.enums.PayType;
import uz.dizgo.erp.enums.StatusTariff;
import uz.dizgo.erp.payload.SubscriptionGetDto;
import uz.dizgo.erp.payload.SubscriptionPostDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T09:58:03+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
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
