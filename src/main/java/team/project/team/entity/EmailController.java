/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.repo.UserRepo;

@RestController
@RequestMapping("/api")
public class EmailController {
    
    @Autowired
    HttpSession session;
    
    @Autowired
    private EmailSenderService mailService;
    
    @Autowired
    private UserRepo userRepo;
    
    //this endpoint call sendmail function
    @GetMapping("/sendmail")
    public String sendMail(@RequestParam String toEmail) throws MessagingException{
        
        mailService.sendMail(toEmail);
        return "Reset email sent to: " + toEmail;
    } 
    @GetMapping("/getcode")
    public String getCode() {
        return mailService.getCode();
    }
    
    @PostMapping("/checkemail")
    public String checkEmail(@RequestParam String newEmail){
        
        Long id = (Long) session.getAttribute("id");
        User user = userRepo.findUserById(id);
        
        
        
        if(userRepo.existsByEmail(newEmail)){
            return "Email is already in use";
            
        }else if(user != null && !userRepo.existsByEmail(newEmail)){
            
            user.setEmail(newEmail);
            userRepo.save(user);
            
            return "Your email has been updated";
        }
        
        return "error";
        
    }
   
    
}
