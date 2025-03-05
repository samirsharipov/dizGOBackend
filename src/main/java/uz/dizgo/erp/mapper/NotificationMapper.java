package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Notification;
import uz.dizgo.erp.payload.NotificationGetAllDto;
import uz.dizgo.erp.payload.NotificationGetByIdDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationGetAllDto toDtoGet(Notification notification);

    List<NotificationGetAllDto> toDtoGetAll(List<Notification> notificationList);


    @Mapping(target = "attachmentId", ignore = true)
    NotificationGetByIdDto toDtoGetById(Notification notification);
}
