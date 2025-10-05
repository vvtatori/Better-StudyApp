package team.project.team.entity;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import team.project.team.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import team.project.team.CustomUserDetailsService;
import team.project.team.repo.CommentRepo;
import team.project.team.repo.GroupRepo;
import team.project.team.repo.MessageRepo;
import team.project.team.repo.PostRepo;



@Service
public class Crud {
    private String email, firstName, lastName, password, username;
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @Autowired
    private UserRepo userRepo;//to access jpa repository and make crud operations
    
    @Autowired
    private MessageRepo msgRepo;
    
    @Autowired
    private PostRepo postRepo;
    
    @Autowired 
    private CommentRepo commentRepo;
    
    @Autowired
    private GroupRepo groupRepo;
    
    @Autowired
    @Lazy
    UserController uc;
    
    @Autowired
    private HttpSession session;  
    
    private boolean loggedIn = false;
    BigInteger upvotes;
    String error;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UserDetailsService userDetailsService;    
    @Autowired
    private AuthenticationManager authenticationManager;   
    
    public String saveUser(String email, String firstName, String lastName, String password, String username) {
        //saves user to database
        if(userRepo.existsByEmail(email)){
            error = "already an exisiting account with this email";
            return "already an exisiting account with this email";
            
        }else if(userRepo.existsByUsername(username)){
           error = "username already exists"; 
            return "username already exists";
        }else{
            if(uc.checkPassword(password)){//method from usercontroller class to check if password meets criteria of a secure password
            String encryptedPassword = passwordEncoder.encode(password);
            
            User userInfo = new User(email, firstName, lastName,encryptedPassword, username,upvotes);
            userInfo.setUpvotes(BigInteger.valueOf(0));
            userRepo.save(userInfo);
            Profile profile = new Profile();
            profile.setUser(userInfo);  // associate profile with the newly created user
            profile.setBio("");         // default empty bio
            profile.setSchool("");      // default empty school
            profile.setCourse("");      // default empty course
            profileService.saveProfile(profile);  // Save profile in DB           


            
            return "User saved successfully";
            }
        }
        if(!uc.checkPassword(password)){
            return "Enter valid password";
        }
        return "Enter valid details";
    }
    
    public String logUser(String username, String password) {
        System.out.println("Login username: " + username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepo.findByUsernameIgnoreCase(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid username or password";
        }    

        if (userDetails == null) {
            return "user not found";
        }

        UsernamePasswordAuthenticationToken authToken = 
        new UsernamePasswordAuthenticationToken(userDetails, password,userDetails.getAuthorities());//compares users details

        try {
            authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authToken); // this sets the users session

            session.setAttribute("loggedIn", true);
            session.setAttribute("id", user.getId());

            return "success";
        } catch (Exception e) {
            session.setAttribute("loggedIn", false);
            return "Invalid username or password";
        }
    }
    public boolean logout(boolean logout){
        
        if(logout){
            session.invalidate();//ends session
            SecurityContextHolder.clearContext();
            return true;
        }
        return false;
    }
    @Transactional //so it rolls back
    public boolean delete(boolean delete){
        
        Long id = (Long) session.getAttribute("id");
        
        
        if(delete){

            msgRepo.deleteAllMessagesById(id);
            postRepo.deleteAllPostsById(id);
            commentRepo.deleteAllCommentsById(id);
            userRepo.deleteById(id);             
            
            return true;
        }
        
        return false;
    }
    
    public String changeUsername(String newUsername,String password){
        
        Long id = (Long) session.getAttribute("id");
        
        User user = userRepo.findUserById(id);
        if(user == null || id == null){
            return "user or id is null";
        }
        if(userRepo.existsByUsername(newUsername)){
            return "Username already exists";
        }else if(passwordEncoder.matches(password, user.getPassword()) && !userRepo.existsByUsername(newUsername)){
            
            user.setUsername(newUsername);
            userRepo.save(user);
            return "Your username has been changed successfully";
        }
        
        return "error";        
    }
    public String changePassword(String oldPassword,String newPassword, String newPasswordTwo){
        
        Long id = (Long)session.getAttribute("id");
        User user = userRepo.findUserById(id);
        
        if(user == null){
            return "user doesnt exist";
        }   
            
            if(uc.checkPassword(newPassword)&& passwordEncoder.matches(oldPassword, user.getPassword())){//this checks if the old password matches the password they enter for validation
               //checks if password meets criteria
                String newEncrypted = passwordEncoder.encode(newPassword);    
                user.setPassword(newEncrypted);//sets new password
                userRepo.save(user);
                return "Successful password change";
            }
            return "error";

    }

}