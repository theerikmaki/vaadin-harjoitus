package online.robodoc.base.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Message
{
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private LocalDateTime timestamp;

    @ManyToOne
    private User sender;

    @ManyToOne
    private ChatRoom chatRoom;

    public Long getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public User getSender()
    {
        return sender;
    }

    public ChatRoom getChatRoom()
    {
        return chatRoom;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setSender(User sender)
    {
        this.sender = sender;
    }

    public void setChatRoom(ChatRoom chatRoom)
    {
        this.chatRoom = chatRoom;
    }
}
