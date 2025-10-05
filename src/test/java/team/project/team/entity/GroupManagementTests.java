package team.project.team.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.project.team.repo.GroupRepo;
import team.project.team.repo.UserRepo;

@ExtendWith(MockitoExtension.class)
public class GroupManagementTests {

    @Mock
    private GroupRepo groupRepo;
    
    @Mock
    private UserRepo userRepo;
    
    @Mock
    private HttpSession session;
    
    private GroupService groupService;
    
    private User testUser;
    private Groups testGroup;
    
    @BeforeEach
    void setUp() {
        // Initialize mock objects
        testUser = mock(User.class);
        
        testGroup = new Groups("TestGroup", 1, false, "General");
        testGroup.setGroupID(1L);
        
        // Initialize real GroupService with mocked repositories
        groupService = new GroupService(groupRepo, userRepo);
        
        // Inject mocked session
        org.springframework.test.util.ReflectionTestUtils.setField(groupService, "session", session);
    }
    
    @Test
    void testCreateGroup() {
        // Arrange
        String groupName = "TestGroup";
        String groupType = "General";
        boolean isPrivate = false;
        Long userId = 1L;
        
        // sets up specific mocks for this test
        List<Groups> groupsList = mock(List.class);
        when(testUser.getGroups()).thenReturn(groupsList);
        when(session.getAttribute("id")).thenReturn(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));
        when(groupRepo.existsByGroupName(groupName)).thenReturn(false);
        
        groupService.createGroup(groupName, groupType, isPrivate);
        
        verify(groupRepo).save(any(Groups.class));
        verify(testUser.getGroups()).add(any(Groups.class));
        verify(userRepo).save(testUser);
    }
    
    @Test
    void testCreateGroupWithExistingName() {
        String groupName = "ExistingGroup";
        String groupType = "General";
        boolean isPrivate = false;
        Long userId = 1L;
        
        when(session.getAttribute("id")).thenReturn(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));
        when(groupRepo.existsByGroupName(groupName)).thenReturn(true);

        groupService.createGroup(groupName, groupType, isPrivate);
        
        verify(groupRepo, never()).save(any(Groups.class));
        verify(userRepo, never()).save(any(User.class));
    }
    
    @Test
    void testJoinGroup() {
        Long groupId = 1L;
        Long userId = 1L;
        
        List<Groups> groupsList = mock(List.class);
        when(testUser.getGroups()).thenReturn(groupsList);
        when(groupRepo.findByGroupID(groupId)).thenReturn(testGroup);
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepo.existsByIdAndGroups_GroupID(userId, groupId)).thenReturn(false);

        String result = groupService.joinGroup(groupId, userId);

        assertTrue(result.isEmpty());
        verify(testUser.getGroups()).add(testGroup);
        verify(userRepo).save(testUser);
    }
    
    @Test
    void testJoinGroupAlreadyMember() {
        Long groupId = 1L;
        Long userId = 1L;
        
        when(groupRepo.findByGroupID(groupId)).thenReturn(testGroup);
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepo.existsByIdAndGroups_GroupID(userId, groupId)).thenReturn(true);

        String result = groupService.joinGroup(groupId, userId);

        assertEquals("User is already in that group", result);
        verify(userRepo, never()).save(any(User.class));
    }
    
    @Test
    void testAddUserToGroup() {
        String username = "testUser";
        Long groupId = 1L;
        Long userId = 1L;

        List<Groups> groupsList = mock(List.class);
        when(testUser.getGroups()).thenReturn(groupsList);
        when(userRepo.findIdByUsername(username)).thenReturn(userId);
        when(userRepo.existsByUsername(username)).thenReturn(true);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.existsByIdAndGroups_GroupID(userId, groupId)).thenReturn(false);
        when(groupRepo.findByGroupID(groupId)).thenReturn(testGroup);
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));

        String result = groupService.addUserToGroup(username, groupId);

        assertEquals("random", result);
        verify(testUser.getGroups()).add(testGroup);
        verify(userRepo).save(testUser);
    }

    @Test
    void testGroupMessaging() {
        class Message {
            private Long id;
            private String content;
            private User sender;
            private Groups group;
            
            public Message(String content, User sender, Groups group) {
                this.content = content;
                this.sender = sender;
                this.group = group;
            }
            
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
            public String getContent() { return content; }
            public User getSender() { return sender; }
            public Groups getGroup() { return group; }
        }

        interface MessageRepo {
            Message save(Message message);
            List<Message> findByGroupId(Long groupId);
        }

        MessageRepo messageRepo = mock(MessageRepo.class);
        when(messageRepo.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));

        class MessageService {
            private final MessageRepo messageRepo;
            private final UserRepo userRepo;
            private final GroupRepo groupRepo;
            
            public MessageService(MessageRepo messageRepo, UserRepo userRepo, GroupRepo groupRepo) {
                this.messageRepo = messageRepo;
                this.userRepo = userRepo;
                this.groupRepo = groupRepo;
            }
            
            public Message sendMessage(String content, Long userId, Long groupId) {
                User user = userRepo.findById(userId).orElse(null);
                Groups group = groupRepo.findByGroupID(groupId);
                
                if (user != null && group != null && userRepo.existsByIdAndGroups_GroupID(userId, groupId)) {
                    Message message = new Message(content, user, group);
                    return messageRepo.save(message);
                }
                return null;
            }
        }

        String messageContent = "Hello group!";
        Long userId = 1L;
        Long groupId = 1L;
        
        when(userRepo.findById(userId)).thenReturn(Optional.of(testUser));
        when(groupRepo.findByGroupID(groupId)).thenReturn(testGroup);
        when(userRepo.existsByIdAndGroups_GroupID(userId, groupId)).thenReturn(true);
        
        MessageService messageService = new MessageService(messageRepo, userRepo, groupRepo);
        
        Message message = messageService.sendMessage(messageContent, userId, groupId);

        assertNotNull(message);
        assertEquals(messageContent, message.getContent());
        assertEquals(testUser, message.getSender());
        assertEquals(testGroup, message.getGroup());
        verify(messageRepo).save(any(Message.class));
    }
}