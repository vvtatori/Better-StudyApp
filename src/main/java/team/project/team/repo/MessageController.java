


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.repo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.entity.Messages;

/**
 *
 * @author samor
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
@Autowired
private MessageRepo msgRepo;
    @RequestMapping("/getMessagesByGroupID")
    public List<Messages> getMessagesByGroupID(Long groupID){//this will be used to old load group chat messages
        return msgRepo.findByGroupID(groupID);
        
    }
}
