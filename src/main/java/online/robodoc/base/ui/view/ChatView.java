package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.Message;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.MessageService;
import online.robodoc.base.service.UserService;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import java.util.List;

@Route(value = "chat", layout = MainLayout.class)
@PageTitle("Global Chat")
public class ChatView extends VerticalLayout
{

    private final MessageService messageService;

    private final UserService userService;

    private final ChatRoomService chatRoomService;

    private final Grid<Message> messageGrid = new Grid<>(Message.class, false);

    private final TextField messageField = new TextField();

    private final Button sendButton = new Button("Send");

    @Autowired
    public ChatView(MessageService messageService, UserService userService, ChatRoomService chatRoomService)
    {
        this.messageService = messageService;

        this.userService = userService;

        this.chatRoomService = chatRoomService;

        if (!SessionUtils.isLoggedIn())
        {
            getUI().ifPresent(ui -> ui.navigate(""));
            return;
        }

        H2 title = new H2("Global Chat Room");

        messageGrid.addColumn(msg -> msg.getSender() != null ? msg.getSender().getUsername() : "-").setHeader("Sender");
        messageGrid.addColumn(Message::getContent).setHeader("Message");
        messageGrid.addColumn(Message::getTimestamp).setHeader("Sent at");

        messageField.setPlaceholder("Type your message...");

        sendButton.addClickListener(e -> sendMessage());

        HorizontalLayout inputLayout = new HorizontalLayout(messageField, sendButton);

        add(title, messageGrid, inputLayout);

        setSizeFull();

        messageGrid.setSizeFull();

        updateGrid();
    }


    private void sendMessage()
    {
        String content = messageField.getValue();

        User sender = SessionUtils.getUser();

        if (content != null && !content.isEmpty() && sender != null)
        {
            ChatRoom chatRoom = chatRoomService.findAll().stream().findFirst().orElseGet(() ->
            {
                ChatRoom room = new ChatRoom();

                room.setName("Global Room");
                room.setDescription("The main room for all users");
                room.setCreator(sender);

                return chatRoomService.save(room);
            });

            Message message = new Message();

            message.setSender(sender);
            message.setChatRoom(chatRoom);
            message.setContent(content);
            message.setTimestamp(LocalDateTime.now());

            messageService.save(message);

            messageField.clear();

            updateGrid();
        }
    }

    private void updateGrid()
    {
        List<Message> messages = messageService.findAll();
        messageGrid.setItems(messages);
    }
}
