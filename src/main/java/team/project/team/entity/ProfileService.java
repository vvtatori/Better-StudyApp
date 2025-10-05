package team.project.team.entity;

import team.project.team.repo.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.team.dto.ProfileDTO;
import team.project.team.repo.UserRepo;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private UserRepo userRepo;
    
    // Saves a new or updated profile to the database
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
    
    // Retrieves all the progile details for a user
    public ProfileDTO getProfileWithUserDetails(Long id) {
        ProfileDTO profileDTO = profileRepository.findProfileWithUserDetails(id);

        // If profileDTO is null, it means the profile doesn't exist, so create one that is empty
        if (profileDTO == null) {
            User user = userRepo.findById(id).orElse(null);  //find the user by id to create their profile in the profile table
            if (user != null) {
                Profile profile = new Profile();
                profile.setUser(user);
                profile.setBio("");
                profile.setSchool("");
                profile.setCourse("");
                profile.setProfileImage("darkmode.png"); 
                profileRepository.save(profile);  //save the data in the database

                // Return the newly created profileDTO
                return new ProfileDTO("", "", "", "darkmode.png", user.getUsername(), user.getEmail());
            }
        }
        
        return profileDTO;
    }
    
    public Profile getOrCreateProfileByUserId(Long id) {
        Profile profile = profileRepository.findByUserId(id);
        
        // If profile doesn't exist, create a new one
        if (profile == null) {
            User user = userRepo.findById(id).orElse(null);
            if (user != null) {
                profile = new Profile();
                profile.setUser(user);
                profile.setBio("");
                profile.setSchool("");
                profile.setCourse("");
                profile.setProfileImage("darkmode.png");
                return saveProfile(profile);
            }
        }
        
        return profile;
    }
    // used to update the profile data when the user edits
    public String updateProfile(Long id, String bio, String school, String course) {
        Profile profile = getOrCreateProfileByUserId(id);
        
        if (profile == null) {
            return "User not found!";
        }
        //set the new values
        profile.setBio(bio);
        profile.setSchool(school);
        profile.setCourse(course);
        saveProfile(profile); //save the values into the database
        return "Profile saved successfully!";
    }
    
    public Profile getProfileByUserId(Long id) {
        return getOrCreateProfileByUserId(id);
    }
}