/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.project.team.entity.Messages;

/**
 *
 * @author samor
 */
public interface MessageRepo extends JpaRepository<Messages,Long> {
        String findGroupNameByGroupID(Long groupID);    
        List<Messages> findByGroupID(Long groupID);
        void deleteAllMessagesById(Long id);
}
