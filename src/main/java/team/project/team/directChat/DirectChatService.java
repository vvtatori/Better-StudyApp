/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.directChat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.project.team.repo.DirectChatRepository;

/**
 *
 * @author vvtat
 */
@Service
public class DirectChatService {
    @Autowired
    private DirectChatRepository repository;

    public List<DirectChat> getChatBetweenUsers(Long user1, Long user2) {
        return repository.findBySenderIDAndReceiverIDOrReceiverIDAndSenderID(user1, user2, user1, user2);
    }

    public DirectChat saveMessage(DirectChat msg) {
        return repository.save(msg);
    }
}
