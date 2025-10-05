/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.project.team.entity.User;
import team.project.team.posts.Posts;

/**
 *
 * @author samor
 */
public interface PostRepo extends JpaRepository<Posts,Long>{
    

    List<Posts> findFirst25ByOrderByTimeSentDesc();
    Posts findByPostID(Long postID);
    void deleteAllPostsById(Long id);
    
    
    
}
