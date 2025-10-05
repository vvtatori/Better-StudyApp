/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author samor
 */
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageID;
    
    @Column 
    private Long groupID;
    
    @Column
    private Long id;    
    
    @Column
    private String username;

    @Column(columnDefinition = "LONGTEXT")
    private String messageContent;
   
    @Column
    private LocalDateTime timeSent;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public Messages(Long messageID, Long groupID, Long id, String username, String messageContent, LocalDateTime timeSent) {
        this.messageID = messageID;
        this.groupID = groupID;
        this.id = id;
        this.username = username;
        this.messageContent = messageContent;
        this.timeSent = timeSent;
    }
    public Messages(){
        
    }
    
    
    
}
