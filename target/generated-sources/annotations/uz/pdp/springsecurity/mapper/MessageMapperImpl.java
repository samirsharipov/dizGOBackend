package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Message;
import uz.pdp.springsecurity.payload.MessageDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-06T21:34:29+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public Message toEntity(MessageDto messageDto) {
        if ( messageDto == null ) {
            return null;
        }

        Message message = new Message();

        message.setSenderId( messageDto.getSenderId() );
        message.setReceiverId( messageDto.getReceiverId() );
        message.setReceiverName( messageDto.getReceiverName() );
        message.setSenderName( messageDto.getSenderName() );
        message.setMessage( messageDto.getMessage() );
        message.setDate( messageDto.getDate() );
        message.setStatus( messageDto.getStatus() );

        return message;
    }

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageDto messageDto = new MessageDto();

        messageDto.setSenderId( message.getSenderId() );
        messageDto.setReceiverId( message.getReceiverId() );
        messageDto.setReceiverName( message.getReceiverName() );
        messageDto.setSenderName( message.getSenderName() );
        messageDto.setMessage( message.getMessage() );
        messageDto.setDate( message.getDate() );
        messageDto.setStatus( message.getStatus() );

        return messageDto;
    }

    @Override
    public List<MessageDto> toDtoList(List<Message> messageList) {
        if ( messageList == null ) {
            return null;
        }

        List<MessageDto> list = new ArrayList<MessageDto>( messageList.size() );
        for ( Message message : messageList ) {
            list.add( toDto( message ) );
        }

        return list;
    }
}
