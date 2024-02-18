package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Message;
import uz.pdp.springsecurity.payload.MessageDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Message toEntity(MessageDto messageDto);

    MessageDto toDto(Message message);

    List<MessageDto> toDtoList(List<Message> messageList);
}
