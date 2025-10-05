package team.project.team.entity;

import java.math.BigInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.project.team.repo.UserRepo;
import team.project.team.entity.UserController;
import team.project.team.entity.ProfileService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SignupTest {
    @Mock
    private UserRepo userRepo;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private UserController uc; 
    
    @Mock
    private ProfileService profileService;
    
    @InjectMocks
    private Crud crud;
    
    private String testEmail = "sam@gmail.com";
    private String testFirstName = "Test";
    private String testLastName = "User";
    private String testPassword = "ValidPass$123";
    private String testUsername = "testuser";
    private String fakeEncodedPassword = "$2a$10$fakeEncodedPasswordForTesting";
    
    @BeforeEach
    void setUp() {
        // This runs before each test
        // Set up the upvotes field in the crud class to zero
        ReflectionTestUtils.setField(crud, "upvotes", BigInteger.ZERO);
    }
    
    @Test
    void testSuccessfulUserSignup() {

        when(userRepo.existsByEmail(testEmail)).thenReturn(false);

        when(userRepo.existsByUsername(testUsername)).thenReturn(false);

        when(uc.checkPassword(testPassword)).thenReturn(true);

        when(passwordEncoder.encode(testPassword)).thenReturn(fakeEncodedPassword);
        
        String result = crud.saveUser(testEmail, testFirstName, testLastName, 
                                     testPassword, testUsername);
        
        assertEquals("User saved successfully", result);

    }
    
    @Test
    void testEmailAlreadyExists() {

        
        when(userRepo.existsByEmail(testEmail)).thenReturn(true);
        
        String result = crud.saveUser(testEmail, testFirstName, testLastName, 
                                     testPassword, testUsername);

        assertEquals("already an exisiting account with this email", result);
        
    }
    
    @Test
    void testUsernameAlreadyExists() {

        when(userRepo.existsByEmail(testEmail)).thenReturn(false);
        
        when(userRepo.existsByUsername(testUsername)).thenReturn(true);
        
        String result = crud.saveUser(testEmail, testFirstName, testLastName, 
                                     testPassword, testUsername);
        
        assertEquals("username already exists", result);
        
    }
    
    @Test
    void testInvalidPassword() {

        when(userRepo.existsByEmail(testEmail)).thenReturn(false);
        when(userRepo.existsByUsername(testUsername)).thenReturn(false);
        
        when(uc.checkPassword(testPassword)).thenReturn(false);
        
        String result = crud.saveUser(testEmail, testFirstName, testLastName, 
                                     testPassword, testUsername);
        

        assertEquals("Enter valid password", result);
        

    }
    
    @Test
    void testEmptyFields() {

        String result = crud.saveUser("", "", "", "", "");

        verify(userRepo).existsByEmail("");
        
    }
}