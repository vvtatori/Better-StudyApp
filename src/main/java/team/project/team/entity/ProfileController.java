package team.project.team.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.project.team.dto.ProfileDTO;
import team.project.team.repo.UserRepo;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private UserService userService;

    //used to update the user's profile
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProfile(
        @PathVariable Long id,
        @RequestParam String bio,
        @RequestParam String school,
        @RequestParam String course) {
        // check if user exists first
        if (!userRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profileService.updateProfile(id, bio, school, course));  //edit the editable parts
    }

    //used to fetch the profile data of the user
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        // First, check if the user exists
        if (!userRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        ProfileDTO profileDTO = profileService.getProfileWithUserDetails(id);  //using profile DTO instead of profile class to get the user profile details including thos in the user table
        return ResponseEntity.ok(profileDTO);
//        Profile profile = profileService.getOrCreateProfileByUserId(id);
//        return ResponseEntity.ok(profile);
    }
    
    //used to get the upvotes of the user
    @GetMapping("/upvotes/{id}")
    public ResponseEntity<UserUpvoteDTO> getUserUpvotes(@PathVariable Long id) {
        UserUpvoteDTO userUpvoteDTO = userService.getUserUpvotes(id);
        if (userUpvoteDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userUpvoteDTO);
    }
}