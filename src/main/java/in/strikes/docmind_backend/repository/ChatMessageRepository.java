package in.strikes.docmind_backend.repository;
import in.strikes.docmind_backend.entity.ChatMessage;
import in.strikes.docmind_backend.entity.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation_IdOrderByCreatedAtAsc(String conversationId);

    //    JPQL query banana

    @Query(value = "SELECT * FROM chat_messages WHERE conversation_id = :conversationId ORDER BY created_at DESC LIMIT :lastN", nativeQuery = true)
    List<ChatMessage> findLastNMessages(@Param("conversationId") String conversationId, @Param("lastN") int lastN);
    void deleteByConversation_Id(String conversationId);

    Optional<ChatMessage> findFirstByConversation_IdAndMessageTypeOrderByCreatedAtDesc(
            String conversationId,
            MessageType messageType
    );

}
