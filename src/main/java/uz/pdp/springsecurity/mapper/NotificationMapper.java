package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Notification;
import uz.pdp.springsecurity.payload.NotificationGetAllDto;
import uz.pdp.springsecurity.payload.NotificationGetByIdDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationGetAllDto toDtoGet(Notification notification);

    List<NotificationGetAllDto> toDtoGetAll(List<Notification> notificationList);


    @Mapping(target = "attachmentId", ignore = true)
    NotificationGetByIdDto toDtoGetById(Notification notification);
}
