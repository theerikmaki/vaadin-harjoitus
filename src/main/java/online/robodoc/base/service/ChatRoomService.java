package online.robodoc.base.service;

import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.User;
import online.robodoc.base.repository.ChatRoomRepository;
import online.robodoc.base.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService
{
    @Autowired
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

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public void delete(ChatRoom chatRoom, User currentUser)
    {
        if (chatRoom.getCreator().getId().equals(currentUser.getId()) || currentUser.getisAdmin())
        {
            messageRepository.deleteByChatRoomId(chatRoom.getId());

            chatRoomRepository.delete(chatRoom);
        }
        else
        {
            throw new RuntimeException("Not authorized to delete this chat room.");
        }
    }
}
