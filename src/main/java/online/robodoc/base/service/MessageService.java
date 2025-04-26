package online.robodoc.base.service;

import online.robodoc.base.domain.Message;
import online.robodoc.base.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService
{
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message)
    {
        return messageRepository.save(message);
    }

    public List<Message> findAll()
    {
        return messageRepository.findAll();
    }

    public List<Message> findByChatRoomId(Long chatRoomId)
    {
        return messageRepository.findByChatRoomId(chatRoomId);
    }
}
