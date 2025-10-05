/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.project.team.entity.User;
import team.project.team.repo.UserRepo;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepo.findByUsernameIgnoreCase(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new org.springframework.security.core.userdetails.User(//gets details from database and stores it to be compared later
            user.getUsername(),
            user.getPassword(),
            Collections.emptyList()
        );
    }
}

