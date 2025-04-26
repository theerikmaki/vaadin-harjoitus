package online.robodoc.base.repository;

import online.robodoc.base.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>
{
    List<UserChatRoom> findByUserId(Long userId);
    List<UserChatRoom> findByChatRoomId(Long chatRoomId);
}
