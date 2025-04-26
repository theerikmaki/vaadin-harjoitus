package online.robodoc.base.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class UserChatRoom
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ChatRoom chatRoom;

    public Long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public ChatRoom getChatRoom()
    {
        return chatRoom;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setChatRoom(ChatRoom chatRoom)
    {
        this.chatRoom = chatRoom;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
