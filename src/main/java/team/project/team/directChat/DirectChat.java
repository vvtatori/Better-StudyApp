/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.directChat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import team.project.team.entity.User;

/**
 *
 * @author vvtat
 */
@Entity
@Table(name = "direct_messages")
public class DirectChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageid")
    private Long messageID;

//    @ManyToOne
//    @JoinColumn(name = "sender_id", nullable = false)
//    private User sender;
//
//    @ManyToOne
//    @JoinColumn(name = "receiver_id", nullable = false)
//    private User receiver;
//
//    @Column(nullable = false)
//    private String content;
//
//    @Column(nullable = false)
//    private LocalDateTime timestamp;

    private Long senderID;
    private Long receiverID;

    private String messageContent;
    private String senderUsername; //to hold the name of the sender

    private LocalDateTime timeSent; //to hold the time stamp

    public DirectChat() {}

    public DirectChat(Long senderID, Long receiverID, String messageContent, String senderUsername, LocalDateTime timeSent) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.messageContent = messageContent;
        this.senderUsername = senderUsername;
        this.timeSent = timeSent;
    }

    public Long getMessageID() {
        return messageID;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
    
}
