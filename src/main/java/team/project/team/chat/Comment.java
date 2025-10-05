/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author samor
 */
@Entity
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long commentID;

    @Column
    private Long id;
    
    @Column
    private String username;
    
    @Column
    private Long postID;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String commentContent;    
    
    @Column
    private LocalDateTime timeSent;


    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Comment(Long id, Long postID, String username, String commentContent, LocalDateTime timeSent) {
        this.id = id;
        this.username = username;
        this.postID = postID;
        this.commentContent = commentContent;
        this.timeSent = timeSent;
    }


    public Comment(){
        
    }
    
    
    
    
            
    
    
}
