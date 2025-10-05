/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.project.team.entity.Groups;

/**
 *
 * @author samor
 */
public interface GroupRepo extends JpaRepository<Groups,Long> {
    
        boolean existsByGroupID(Long groupID);
        Groups findByGroupID(Long groupID);
        boolean existsByGroupName(String groupName);
        Groups findByGroupName(String groupName);  
        String findGroupNameByGroupID(Long groupID);  
        
        @Query("SELECT g FROM Groups g JOIN g.users u WHERE u.id = :id")
        List<Groups> findAllById(@Param("id") Long id);         
        List<Groups> findByPrivacyOn(boolean privacyOn);
        

}
