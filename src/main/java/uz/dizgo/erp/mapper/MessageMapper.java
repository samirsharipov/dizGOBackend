package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Message;
import uz.dizgo.erp.payload.MessageDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message toEntity(MessageDto messageDto);

    @Mapping(target = "attachmentId", ignore = true)
    MessageDto toDto(Message message);

    List<MessageDto> toDtoList(List<Message> messageList);
}
