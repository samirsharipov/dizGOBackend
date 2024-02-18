package uz.pdp.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.Message;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.mapper.MessageMapper;
import uz.pdp.springsecurity.payload.ApiResponse;
import uz.pdp.springsecurity.payload.MessageDto;
import uz.pdp.springsecurity.repository.AttachmentRepository;
import uz.pdp.springsecurity.repository.MessageRepository;
import uz.pdp.springsecurity.repository.UserRepository;

import java.util.*;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessageDto receiveMessage(@Payload MessageDto messageDto) throws InterruptedException {
        Message message = new Message();
        message.setMessage(messageDto.getMessage());
        message.setReceiverId(messageDto.getReceiverId());
        if (messageDto.getAttachmentId() != null) {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(messageDto.getAttachmentId());
            optionalAttachment.ifPresent(message::setAttachment);
        }
        message.setSenderId(messageDto.getSenderId());
        messageRepository.save(message);
        Thread.sleep(1000);
        return messageDto;
    }

    @MessageMapping("/private-message")
    public MessageDto recMessage(@Payload MessageDto messageDto) {
        simpMessagingTemplate.convertAndSendToUser(messageDto.getReceiverName(), "/private", messageDto);
        return messageDto;
    }

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
