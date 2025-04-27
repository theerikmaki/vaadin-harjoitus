package online.robodoc.base.repository;

import online.robodoc.base.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>
{
    List<Message> findByChatRoomId(Long chatRoomId);

    @Transactional
    void deleteByChatRoomId(Long chatRoomId);
}
