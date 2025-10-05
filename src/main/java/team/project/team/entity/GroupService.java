
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.team.repo.GroupRepo;
import team.project.team.repo.UserRepo;

/**
 *
 * @author samor
 */
@Service
public class GroupService {
    
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private HttpSession session;    
    @Autowired
    private UserRepo userRepo;
    boolean exists = false;
    boolean existsid = false;

    public GroupService(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }
    public String addUserToGroup(String username,Long groupID){
        
            
            Long id = userRepo.findIdByUsername(username);
            if(id == null){
                id = Long.parseLong(username);
            }
            exists = userRepo.existsByUsername(username);
            existsid = userRepo.existsById(id);
            
        if((exists || existsid)&& groupID != null && !isUserInGroup(id,groupID)){
            Groups groupInfo = groupRepo.findByGroupID(groupID);
            if(existsid){
                User user = userRepo.findById(id).orElse(null);              
                user.getGroups().add(groupInfo);
                userRepo.save(user);                
            }else if(exists){
                User user = userRepo.findById(id).orElse(null);              
                user.getGroups().add(groupInfo);
                userRepo.save(user);                 
            }else{
                return "error";
            }
             
    
        }else{
            return "username or groupID doesnt exist";
        }
        return "random";
    }

    public void createGroup(String groupName,String groupType,boolean privacyOn) {

        int memberAmount = 1; // The only member is the creator of the group
        //Long id = (Long) session.getAttribute("id");//retrievies id from when the user logged in
        //session.setAttribute("id",5L);//get rid of this for deployment
        
        Long id = (Long)session.getAttribute("id");

           if (id == null) {
               System.out.println("id null");
           }
           
            User user = userRepo.findById(id).orElse(null);  


            
        if (groupName != null && groupType != null && !groupRepo.existsByGroupName(groupName)) { // checks if groupName already exists
            Groups groupInfo = new Groups(groupName, memberAmount, privacyOn, groupType);
            groupRepo.save(groupInfo);
            user.getGroups().add(groupInfo);  //this connect the group to the user's groups
            userRepo.save(user);  // saves the user with the updated group list
        }
    }
    public boolean isUserInGroup(Long id, Long groupID) {
        // Call the repository method to check if the user is part of the group
        return userRepo.existsByIdAndGroups_GroupID(id,groupID);
    }
    public String joinGroup(Long groupID,Long id){
        
        if(groupID != null && id != null){
            Groups groupInfo = groupRepo.findByGroupID(groupID);
            User user = userRepo.findById(id).orElse(null);  
            
            if(user != null && !isUserInGroup(id,groupID)){//checks if user is already in that group
                user.getGroups().add(groupInfo);
                userRepo.save(user);                
            }else{
                return "User is already in that group";
            }

        }
        return "";
    }
    
}
   
    

    

