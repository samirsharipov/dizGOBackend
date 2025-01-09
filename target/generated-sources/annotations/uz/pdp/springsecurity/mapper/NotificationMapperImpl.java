package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Notification;
import uz.pdp.springsecurity.payload.NotificationGetAllDto;
import uz.pdp.springsecurity.payload.NotificationGetByIdDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-09T14:24:57+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationGetAllDto toDtoGet(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationGetAllDto notificationGetAllDto = new NotificationGetAllDto();

        notificationGetAllDto.setId( notification.getId() );
        notificationGetAllDto.setName( notification.getName() );
        notificationGetAllDto.setRead( notification.isRead() );
        notificationGetAllDto.setCreatedAt( notification.getCreatedAt() );
        notificationGetAllDto.setObjectId( notification.getObjectId() );
        notificationGetAllDto.setBranchId( notification.getBranchId() );

        return notificationGetAllDto;
    }

    @Override
    public List<NotificationGetAllDto> toDtoGetAll(List<Notification> notificationList) {
        if ( notificationList == null ) {
            return null;
        }

        List<NotificationGetAllDto> list = new ArrayList<NotificationGetAllDto>( notificationList.size() );
        for ( Notification notification : notificationList ) {
            list.add( toDtoGet( notification ) );
        }

        return list;
    }

    @Override
    public NotificationGetByIdDto toDtoGetById(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationGetByIdDto notificationGetByIdDto = new NotificationGetByIdDto();

        notificationGetByIdDto.setName( notification.getName() );
        notificationGetByIdDto.setMessage( notification.getMessage() );
        if ( notification.getType() != null ) {
            notificationGetByIdDto.setType( notification.getType().name() );
        }
        notificationGetByIdDto.setObjectId( notification.getObjectId() );
        notificationGetByIdDto.setBranchId( notification.getBranchId() );

        return notificationGetByIdDto;
    }
}
