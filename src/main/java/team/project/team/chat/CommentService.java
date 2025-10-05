/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.chat;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.team.entity.User;
import team.project.team.repo.CommentRepo;
import team.project.team.repo.UserRepo;

/**
 *
 * @author samor
 */
@Service
public class CommentService {
 
   @Autowired
   HttpSession session;
   
   @Autowired
   UserRepo userRepo;
   @Autowired
   CommentRepo commentRepo;
   LocalDateTime timeSent;
   
   public String sendComment(Long postID, Long id,String username,String commentContent){

        boolean user = userRepo.existsById(id);
        
        if(!user || id == null || commentContent == null){
            return "something is null";
        }

        if(user && id != null && commentContent != null){
            
            Comment commentInfo = new Comment(id,postID,username,commentContent,timeSent);
            
            commentInfo.setId(id);
            commentInfo.setPostID(postID);
            commentInfo.setCommentContent(commentContent);            
            commentInfo.setTimeSent(LocalDateTime.now());        
            
            commentRepo.save(commentInfo);  
            return "Successful";            
            
        }else{
            return "Error: something is null";
        }
       
       
   }
        
       

       
       
       
   
       
    
}
