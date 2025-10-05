/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.project.team.chat.Comment;

/**
 *
 * @author samor
 */
public interface CommentRepo extends JpaRepository<Comment,Long> {
    
    List<Comment> findAllByPostID(Long postID);
    void deleteAllCommentsById(Long id);
    
    
    
    
}
