package team.project.team.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import team.project.team.repo.UserRepo;

@RestController
@RequestMapping("/api")
public class ResetPassword {

    @Autowired
    private UserRepo userRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    @GetMapping("/redirectChangePassword")
    public String redirectUser(@RequestParam boolean redirect){
        System.out.println(redirect);
            
            return "redirect:/changepassword.html";
    }
 
    @PostMapping("/getPass")
    public String resetPassword(@RequestParam String password, @RequestParam String storedEmail) {
        // Find user by email\

        User user = userRepo.findByEmail(storedEmail);

        if (user == null) {
            return "User not found";
        }

        // Encrypt and update password
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepo.save(user);

        return "Password updated successfully";
    }
    
    
}
