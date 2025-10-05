/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.repo.GroupRepo;
import team.project.team.repo.UserRepo;


@RestController
@RequestMapping("/api/groups")
public class GroupController {
    
@Autowired
private GroupRepo groupRepo;    
@Autowired
private UserRepo userRepo;
@Autowired
HttpSession session;

private final GroupService groupService;

    
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    
    @GetMapping()
    public List<Groups> getAllGroups(HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        return groupRepo.findAllById(id);
    }
    
    @PostMapping("/creategroup")
    public void createGroup(@RequestParam String groupName,@RequestParam String groupType,@RequestParam String privacyOn){
    boolean isPrivate = Boolean.parseBoolean(privacyOn);
       groupService.createGroup(groupName,groupType,isPrivate);

    }
    @PostMapping("/adduser")
    public void addUserToGroup(@RequestParam String username,@RequestParam Long groupID){
        groupService.addUserToGroup(username, groupID);
    }
    
    @GetMapping("/publicgroups")
    public List<Groups> returnPublicGroups(){
        
        return groupRepo.findByPrivacyOn(false);
              
    }
    @PostMapping("/joingroup")
    public void joinGroup(@RequestParam Long groupID){
        Long id = (Long) session.getAttribute("id");
        
        groupService.joinGroup(groupID,id);
    } 
    
}
