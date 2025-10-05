/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.project.team.entity.User;
import team.project.team.entity.UserUpvoteDTO;

/**
 *
 * @author samor
 */
public interface UserRepo extends JpaRepository<User, Long>{
    
        boolean existsByEmail(String email);
        boolean existsByUsername(String username);
        User findByEmail(String email);
        User findByUsernameIgnoreCase(String username); 
        User findByPassword(String password);     
        @Query("SELECT u.id FROM User u WHERE u.username = :username")
        Long findIdByUsername(@Param("username") String username); 
        Long findIdByUsernameIgnoreCase(@Param("username") String username);         
        @Query("SELECT u.username FROM User u WHERE u.id = :id")
        String findUsernameById(@Param("id") Long id);

         User findUserById(Long id);
        
        
        @Query("SELECT u.username, u.upvotes FROM User u ORDER BY upvotes DESC LIMIT 10")//this get top 10 upvotes and username by highest upvotes in a descending order 
        List<Object[]> findAllUsernameAndUpvotes();
        boolean existsByIdAndGroups_GroupID(Long id, Long groupID);        
        
        @Query("SELECT new team.project.team.entity.UserUpvoteDTO(u.username, u.upvotes) FROM User u WHERE u.id = :id")
        UserUpvoteDTO findUserUpvotesById(@Param("id") Long id);
    
}
