/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.posts;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.project.team.entity.GroupService;
import team.project.team.entity.Groups;
import team.project.team.entity.User;
import team.project.team.repo.PostRepo;
import team.project.team.repo.UserRepo;

/**
 *
 * @author samor
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private final PostService postService;
    @Autowired PostRepo postRepo;
   
    public PostController(PostService postService) {
        this.postService = postService;
    } 
        //KB tutorials reference - youtube channel
    @PostMapping(value = "/getFileInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE) //takes file in
    public Posts getFileInfo(@RequestParam(value = "file", required = false)MultipartFile userFile) throws IOException{
        
    Posts fileInfo = new Posts();
    
    if (userFile != null && !userFile.isEmpty()) {
        fileInfo.setFileName(userFile.getOriginalFilename());
        fileInfo.setContentType(userFile.getContentType());
        fileInfo.setFileLength(userFile.getSize());
        fileInfo.setIsFileEmpty(userFile.isEmpty());
        fileInfo.setIsReadable(userFile.getResource().isReadable());
        fileInfo.setFileData(userFile.getBytes());
    } else {
        // Handle the case where no file is provided (optional)
        fileInfo.setFileName("No file provided");
        fileInfo.setContentType("N/A");
        fileInfo.setFileLength(0L);
        fileInfo.setIsFileEmpty(true);
        fileInfo.setIsReadable(false);
        fileInfo.setFileData(new byte[0]); // Empty byte array for no file
    }
    
    return fileInfo;
    
}
    
       
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPost(@RequestParam String postContent, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        
        Posts fileInfo = new Posts();
        
        if(file != null && !file.isEmpty()) {
            fileInfo = getFileInfo(file);
        }else{
            fileInfo.setFileName("No file provided");
            fileInfo.setContentType("N/A");
            fileInfo.setFileLength(0L);
            fileInfo.setIsFileEmpty(true);
            fileInfo.setIsReadable(false);
            fileInfo.setFileData(new byte[0]);         
        }
        postService.createPost(postContent, fileInfo);
        return "Post created successfully!";
    }
    @GetMapping
    public List<Posts> getAllPosts() {
        List<Posts> posts = postRepo.findFirst25ByOrderByTimeSentDesc();

        // converts each post's fileData to Base64 string
        for (Posts post : posts) {
            if (post.getFileData() != null) {
                String base64File = Base64.getEncoder().encodeToString(post.getFileData());
                post.setBase64File(base64File); // adds the Base64 string to the post
            }
        }

        return posts;
    }
    @PostMapping("/{postID}/like")
    public String likePost(@RequestParam Long postID){
        
        Posts post = postRepo.findByPostID(postID);
        Long id = post.getId();
        User user = userRepo.findById(id).orElse(null);
        
        
        
        user.setUpvotes(user.getUpvotes().add(BigInteger.valueOf(1)));
        userRepo.save(user);
        post.setPostLikes(post.getPostLikes().add(BigInteger.valueOf(1)));
        postRepo.save(post);
        
        return user.getUpvotes().toString();
        
    }
    
    
}
