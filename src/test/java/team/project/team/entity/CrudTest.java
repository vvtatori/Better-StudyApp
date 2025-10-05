package team.project.team.entity;

import jakarta.servlet.http.HttpSession;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import team.project.team.repo.UserRepo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Crud class login functionality.
 */
@ExtendWith(MockitoExtension.class)
class CrudTest {

    @Mock private UserRepo userRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private HttpSession session;
    @Mock private UserDetailsService userDetailsService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private Crud crud;

    private User testUser;
    private final String rawPassword = "Sam$";
    private final String encodedPassword = "$2a$10$testEncodedPassword"; // i used bcyrpt in my login so this is a made up encrpyted password for testing

    @BeforeEach
    void setUp() {
        testUser = new User(// this setups a fake user for testing
            "sam@gmail.com",
            "Test",
            "User",
            encodedPassword,
            "testuser",
            BigInteger.ZERO
        );
    }

    @Test
    void logUserValidDetails() {
        
        when(userRepo.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);//search for mock user and returns the user
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);//checks if match

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUserDetails);//retrieves details about user from the mock user we created above
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));//simulates authentication
        doNothing().when(session).setAttribute(anyString(), any());//gives session id for testuser

        String result = crud.logUser("testuser", rawPassword);//simulates valid details

        assertEquals("success", result);
    }

    @Test
    void logUserInvalidDetails() {

        when(userRepo.findByUsernameIgnoreCase("invaliduser")).thenReturn(null);//simulates finding user in db

        String result = crud.logUser("invaliduser", "somePassword");//testing username with mock password

        assertEquals("Invalid username or password", result);
    }

    @Test
    void logUserInvalidPassword() {

        when(userRepo.findByUsernameIgnoreCase("testuser")).thenReturn(testUser);//simulates finding user in db
        when(passwordEncoder.matches("WrongPass123", encodedPassword)).thenReturn(false);//simulates wrong password

        String result = crud.logUser("testuser", "WrongPass123");//simulates login

        assertEquals("Invalid username or password", result);
    }
}
