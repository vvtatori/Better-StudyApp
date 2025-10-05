/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.directChat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.entity.User;
import team.project.team.repo.DirectChatRepository;
import team.project.team.repo.UserRepo;

/**
 *
 * @author vvtat
 */
@RestController
@RequestMapping("/api/directchat")
public class DirectChatController {
     @Autowired
    private DirectChatRepository directChatRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
private UserRepo userRepo;

    // REST Endpoint: Get all DMs between two users
    @GetMapping("/getMessages")
    public List<DirectChat> getMessagesBetweenUsers(@RequestParam Long user1, @RequestParam Long user2) {
        return directChatRepository.findBySenderIDAndReceiverIDOrReceiverIDAndSenderID(user1, user2, user1, user2);
    }

    // WebSocket Endpoint: Send DM message
    @MessageMapping("/directChat")
    public void sendDirectMessage(@Payload DirectChat chatMessage) {
        if (chatMessage.getSenderID() == null || chatMessage.getReceiverID() == null) {
            System.out.println("Missing sender or receiver ID");
            return;
        }
        
        chatMessage.setTimeSent(LocalDateTime.now());
        chatMessage.setSenderUsername(userRepo.findUsernameById(chatMessage.getSenderID()));
        directChatRepository.save(chatMessage);

        // Send to both sender and receiver's topic
        String topic1 = "/topic/user/" + chatMessage.getSenderID() + "/" + chatMessage.getReceiverID();
        String topic2 = "/topic/user/" + chatMessage.getReceiverID() + "/" + chatMessage.getSenderID();

        messagingTemplate.convertAndSend(topic1, chatMessage);
        messagingTemplate.convertAndSend(topic2, chatMessage);
    }  
    
    // Get list of chat users for the current user
    @GetMapping("/userChats")
    public ResponseEntity<List<User>> getChatUsers(@RequestParam Long userId) {
        List<DirectChat> chats = directChatRepository.findBySenderIDOrReceiverID(userId, userId);

        Set<Long> userIds = new HashSet<>();
        for (DirectChat chat : chats) {
            if (!chat.getSenderID().equals(userId)) userIds.add(chat.getSenderID());
            if (!chat.getReceiverID().equals(userId)) userIds.add(chat.getReceiverID());
        }

        List<User> users = userRepo.findAllById(userIds);
        return ResponseEntity.ok(users);
    }
    
}
