/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.chat;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.entity.GroupService;
import team.project.team.entity.User;
import team.project.team.repo.CommentRepo;
import team.project.team.repo.UserRepo;

/**
 *
 * @author samor
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    HttpSession session;
    
    private CommentService commentService;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    UserRepo userRepo;
    String username;
    
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }    
    @PostMapping("/sendcomment")
    public void sendComment(@RequestParam Long postID,@RequestParam String commentContent){

        Long id = (Long) session.getAttribute("id");
        
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            username = user.get().getUsername();
        }
        commentService.sendComment(postID, id,username, commentContent);
        
        
    }
    @GetMapping("/getComments")
    public List<Comment> getComments(Long postID){
        return commentRepo.findAllByPostID(postID);
    }
    
}
