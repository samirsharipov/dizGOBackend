package uz.dizgo.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.dizgo.erp.entity.Message;
import uz.dizgo.erp.entity.User;
import uz.dizgo.erp.mapper.MessageMapper;
import uz.dizgo.erp.payload.ApiResponse;
import uz.dizgo.erp.payload.MessageDto;
import uz.dizgo.erp.repository.AttachmentRepository;
import uz.dizgo.erp.repository.MessageRepository;
import uz.dizgo.erp.repository.UserRepository;

import java.util.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatController {

//    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper mapper;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    @Qualifier("sessionRegistry")
    private final SessionRegistry sessionRegistry;

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public MessageDto receiveMessage(@Payload MessageDto messageDto) throws InterruptedException {
//        Message message = new Message();
//        message.setMessage(messageDto.getMessage());
//        message.setReceiverId(messageDto.getReceiverId());
//        if (messageDto.getAttachmentId() != null) {
//            Optional<Attachment> optionalAttachment = attachmentRepository.findById(messageDto.getAttachmentId());
//            optionalAttachment.ifPresent(message::setAttachment);
//        }
//        message.setSenderId(messageDto.getSenderId());
//        messageRepository.save(message);
//        Thread.sleep(1000);
//        return messageDto;
//    }

//    @MessageMapping("/private-message")
//    public MessageDto recMessage(@Payload MessageDto messageDto) {
//        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverName(), "/private", messageDto);
//        return messageDto;
//    }

    @GetMapping("/api/{senderId}/{receivedId}")
    public HttpEntity<?> getMessageList(@PathVariable UUID senderId, @PathVariable UUID receivedId) {
        List<Message> all = messageRepository.findAllBySenderIdAndReceiverId(senderId, receivedId);
        all.addAll(messageRepository.findAllBySenderIdAndReceiverId(receivedId, senderId));
        all.sort(Comparator.comparing(Message::getCreatedAt));
        if (all.isEmpty()) {
            return ResponseEntity.status(409).body(new ApiResponse("Bo'sh", false));
        }
        List<MessageDto> mapperDtoList = mapper.toDtoList(all);
        return ResponseEntity.status(200).body(new ApiResponse("all", true, mapperDtoList));
    }

    @GetMapping("/api/getUsers/{businessId}")
    public HttpEntity<?> getUsers(@PathVariable UUID businessId) {
        List<User> allByBusinessId = userRepository.findAllByBusiness_Id(businessId);
        return ResponseEntity.status(200).body(new ApiResponse("all", true, allByBusinessId));
    }

    @GetMapping("/getOnlineUsers")
    public HttpEntity<?> getOnlineUsers() {
        List<User> retValue = new ArrayList<User>();
        Set<String> names = new HashSet<>();
        List<Object> onlineUsers = sessionRegistry.getAllPrincipals();
        for (Object usr : onlineUsers) {
            retValue.add((User) usr);
        }
        for (User user : retValue) {
            names.add(user.getFirstName() + " " + user.getLastName());
        }
        return ResponseEntity.status(200).body(new ApiResponse("all", true, names));
    }


}
