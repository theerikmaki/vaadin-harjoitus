package online.robodoc.base.service;

import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.User;
import online.robodoc.base.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService
{
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository)
    {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom save(ChatRoom chatRoom)
    {
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> findAll()
    {
        return chatRoomRepository.findAll();
    }

    public Optional<ChatRoom> findById(Long id)
    {
        return chatRoomRepository.findById(id);
    }

    public void delete(ChatRoom chatRoom, User currentUser)
    {
        if (chatRoom.getCreator().equals(currentUser) || currentUser.getisAdmin())
        {
            chatRoomRepository.delete(chatRoom);
        }
        else
        {
            throw new RuntimeException("Not authorized to delete this chat room.");
        }
    }
}
