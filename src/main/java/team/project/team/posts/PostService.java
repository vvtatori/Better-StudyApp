/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.posts;

import jakarta.servlet.http.HttpSession;
import java.math.BigInteger;
import java.sql.Blob;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.team.repo.PostRepo;
import team.project.team.repo.UserRepo;

/**
 *
 * @author samor
 */
@Service
public class PostService {

    @Autowired
    private HttpSession session;
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    public void createPost(String postContent, Posts fileInfo) {
        
        
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            id = (Long) session.getAttribute("id");//testing hardcoded values
        }
        String username = userRepo.findUsernameById(id);

        if (postContent == null || postContent.trim().isEmpty()) {
            System.out.println("Post content is empty");
            return;
        }

        //creates new Post 
        Posts newPost = new Posts();
        newPost.setId(id);
        newPost.setUsername(username);
        newPost.setTimeSent(LocalDateTime.now());
        newPost.setPostContent(postContent);
        newPost.setFileName(fileInfo.getFileName());
        newPost.setContentType(fileInfo.getContentType());
        newPost.setFileLength(fileInfo.getFileLength());
        newPost.setFileData(fileInfo.getFileData());
        newPost.setPostLikes(BigInteger.valueOf(0));

        // saves to database
        postRepo.save(newPost);
        System.out.println("Post successfully saved!");
    }
}

