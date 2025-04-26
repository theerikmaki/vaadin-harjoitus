package online.robodoc.base.service;

import online.robodoc.base.domain.UserChatRoom;
import online.robodoc.base.repository.UserChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChatRoomService
{
    private final UserChatRoomRepository userChatRoomRepository;

    public UserChatRoomService(UserChatRoomRepository userChatRoomRepository)
    {
        this.userChatRoomRepository = userChatRoomRepository;
    }

    public UserChatRoom save(UserChatRoom userChatRoom)
    {
        return userChatRoomRepository.save(userChatRoom);
    }

    public List<UserChatRoom> findByUserId(Long userId)
    {
        return userChatRoomRepository.findByUserId(userId);
    }

    public List<UserChatRoom> findByChatRoomId(Long chatRoomId)
    {
        return userChatRoomRepository.findByChatRoomId(chatRoomId);
    }
}
