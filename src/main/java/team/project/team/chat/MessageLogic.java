package team.project.team.chat;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import team.project.team.entity.Messages;
import team.project.team.entity.User;
import team.project.team.repo.MessageController;
import team.project.team.repo.MessageRepo;
import team.project.team.repo.UserRepo;

@Controller
public class MessageLogic {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageController messageController;
    @Autowired
    private MessageRepo msgRepo;
    
    @Autowired UserRepo userRepo;
    
    @Autowired
    HttpSession session;
      
    @MessageMapping("/chat")         
    public Messages sendMessage(@Payload Messages messages) {
        
        //Long id = (Long) session.getAttribute("id"); // used this to test a user already logged in to test group messaging service
        Long id = messages.getId();
        
        // Long sessionId = (Long) session.getAttribute("id");        
        if (id == null && messages.getId() == null) {
            System.out.println("Error: Sender ID is missing!");
            return null;
        }
        
        if (messages.getId() == null) {
            messages.setId(id);
        }
        
        messages.setId(id);
        // stores message timestamp
        messages.setTimeSent(LocalDateTime.now());
        User user = userRepo.findById(id).orElse(null);
        messages.setUsername(user.getUsername());
        // saves the message to the database
        msgRepo.save(messages);
        

        
        // checks if groupID is valid
        Long groupId = messages.getGroupID();
        if (groupId != null) {
            // sends message to subscribers of the group
            messagingTemplate.convertAndSend("/topic/group/" + groupId, messages);
        } else {
            System.out.println("Error: Group ID is missing!");
        }

        return messages;
    }    

    public List<Messages> getMessagesByGroupID(Long groupID){
        return messageController.getMessagesByGroupID(groupID);
    }
}
