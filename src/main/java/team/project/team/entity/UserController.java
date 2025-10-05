/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.project.team.repo.UserRepo;


@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private Crud crud;
    @Autowired    
    private UserRepo userRepo;
    
    @Autowired
    HttpSession session;
    
    private boolean loggedIn;
    String error;
    @Autowired
    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();   
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }   
    @GetMapping("/signup")//when user is on this page, sends a post req
    public String saveUser(@RequestParam String email, @RequestParam String firstName,@RequestParam String lastName,@RequestParam String password,@RequestParam String username) {

        return crud.saveUser(email,firstName,lastName,password,username);//passes data to crud class 
        
    }  
    @PostMapping("/logout")
    public boolean logout(@RequestParam boolean logout){
        return crud.logout(logout);
    }
    @PostMapping("/delete")
    public boolean deleteAccount(@RequestParam boolean delete){
        return crud.delete(delete);
        
    }
    
    @GetMapping("/login")//when user is on this page, sends a post req
    @ResponseBody
    public String logUser( @RequestParam String username,@RequestParam String password) {//passes data to crud class 
       
        String result =  crud.logUser(username,password);
        if ("success".equals(result)) {
            session.setAttribute("SPRING_SECURITY_CONTEXT", 
                SecurityContextHolder.getContext());//stores authetication
        }        
        return result;
    }
    @PostMapping("/passwithuser")
    public String changeUsername(@RequestParam String newUsername,@RequestParam String password){
        
        return crud.changeUsername(newUsername,password);
    }
            
    @PostMapping("/getEncryptPass")
    public String getEncrypt(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String newPasswordTwo){
        
        return crud.changePassword(oldPassword, newPassword, newPasswordTwo);
        
    }
    
   
    public boolean checkPassword(String password){//used to check if password meets security criteria
        boolean validPassword = false;
        String[] symbols = {
            "!", "#", "$", "%", "&", "(", ")", "*", "+", ",", "-", ".", "?", "@", "[", "]", "^", "_", "{", "|", 
            "}", "~"
        };
        for(int i = 0; i < password.length(); i++){
            
           for(int j = 0;j < symbols.length;j++){         
                if(password.charAt(i) == symbols[j].charAt(0)){
                      return true;          
                }
           } 
           
       }
       return false; 
    } 
    @RequestMapping("/user")
    public User getUser(HttpSession session){
        Long id = (Long)session.getAttribute("id");
        User user = userRepo.findUserById(id);
        if(user == null){
            System.out.println("user null");
        }
       return user;
    }
    
    @RequestMapping("/id")
    public Long getUserId(HttpSession session) {
        
        //session.setAttribute("id",5L);//get rid of this for deployment
        Long id  = (Long) session.getAttribute("id");
        //Long id = (Long) session.getAttribute("id");//used to pass id to the javascript so i can use id in payload
        if(id == null){
            return null;
        }        
        return id; 
    }   
    @RequestMapping("/username")
    public String getUsernameById(Long id){       
        String username = String.valueOf(userRepo.findUsernameById(id));
        return username;
    }
    @RequestMapping("/loggedIn")
    private boolean checkLoggedStatus(){
        loggedIn = (boolean)session.getAttribute("loggedIn");
        return loggedIn;
    } 
    @RequestMapping("/error")
    public String getErrorMessage(){
        return error;
    }

    @GetMapping("/leaderboardto")
    public List<UserUpvoteDTO> getAllUsernameAndUpvotesAsDTO() {
        return userService.getAllUsernameAndUpvotesAsDTO();
    }
    
    @GetMapping("/user/exists")
    @ResponseBody
    public boolean checkUserExists(@RequestParam String username) {
        // Check if the username exists in the database
        return userRepo.existsByUsername(username);
    }
    
    //To be used in the direct chat functions
    // Search user by username
    @GetMapping("/users/search")
    public ResponseEntity<User> searchByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }    
}
