package team.project.team.entity;

import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import team.project.team.repo.UserRepo;
///https://www.baeldung.com/java-dto-pattern website to finish leaderboard
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    
    public List<Object[]> getAllUsernameAndUpvotes() {
        return userRepo.findAllUsernameAndUpvotes();
    }
    
    public List<UserUpvoteDTO> getAllUsernameAndUpvotesAsDTO() {//method on how the username and upvotes are mapped
        List<Object[]> results = userRepo.findAllUsernameAndUpvotes();
         if (results == null) { 
            return List.of();
        }       
        return results.stream()
            .map(result -> new UserUpvoteDTO(
                    (String) result[0], 
                    new BigInteger(result[1].toString())
            ))
            .collect(Collectors.toList());
    }
    
    //Added to get the user upvotes to be used in the profile page
    public UserUpvoteDTO getUserUpvotes(Long id) {
        return userRepo.findUserUpvotesById(id);
    }
    
    //Added to be used in direct chat
    public User findByUsername(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }
}