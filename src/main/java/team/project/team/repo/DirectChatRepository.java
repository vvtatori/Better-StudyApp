package team.project.team.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.project.team.directChat.DirectChat;

/**
 *
 * @author vvtat
 */
public interface DirectChatRepository extends JpaRepository<DirectChat, Long>{
//    List<DirectMessage> findBySender_UsernameAndReceiver_UsernameOrderByTimestamp(String sender, String receiver);
//    @Query("SELECT m FROM DirectMessage m WHERE " +
//           "(m.sender.username = :user1 AND m.receiver.username = :user2) OR " +
//           "(m.sender.username = :user2 AND m.receiver.username = :user1) " +
//           "ORDER BY m.timestamp")
//    List<DirectChat> findChatHistory(@Param("user1") String user1, @Param("user2") String user2);
    List<DirectChat> findBySenderIDAndReceiverIDOrReceiverIDAndSenderID(Long senderID1, Long receiverID1, Long senderID2, Long receiverID2);
    
    List<DirectChat> findBySenderIDOrReceiverID(Long senderId, Long receiverId);
}