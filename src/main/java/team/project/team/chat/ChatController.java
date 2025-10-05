/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.chat;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import team.project.team.entity.Groups;
import team.project.team.entity.Messages;
import team.project.team.repo.GroupRepo;
import team.project.team.repo.MessageRepo;

/**
 *
 * @author samor
 */
public class ChatController {
    
    @Autowired
    private MessageRepo msgRepo;
    @Autowired
    private GroupRepo groupRepo;   
    
    private Messages messages;
    
    public Optional<Groups> findGroupByMessage(Messages messages){
        
        if(messages.getGroupID() != null){
           Groups group = groupRepo.findByGroupID(messages.getGroupID());
           return Optional.ofNullable(group);
        }
        return Optional.empty();
    }
    
}
