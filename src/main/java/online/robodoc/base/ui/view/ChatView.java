package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.Message;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.MessageService;
import online.robodoc.base.service.UserService;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;
import online.robodoc.base.util.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "chat/:chatRoomId", layout = MainLayout.class)
@PageTitle("Chat Room")
public class ChatView extends VerticalLayout implements BeforeEnterObserver
{
    private final MessageService messageService;

    private final UserService userService;

    private final ChatRoomService chatRoomService;

    private final VerticalLayout messageList = new VerticalLayout();

    private final Scroller messageScroller = new Scroller(messageList);

    private final TextArea messageField = new TextArea();

    private final Button sendButton = new Button("Send");

    private ChatRoom currentRoom;

    @Autowired
    public ChatView(MessageService messageService, UserService userService, ChatRoomService chatRoomService)
    {
        this.messageService = messageService;

        this.userService = userService;

        this.chatRoomService = chatRoomService;

        if (!SessionUtils.isLoggedIn())
        {
            getUI().ifPresent(ui -> ui.navigate("login"));

            return;
        }

        configureLayout();

        Broadcaster.register(this);

        addDetachListener(e -> Broadcaster.unregister(this));
    }

    public void reloadMessages()
    {
        if (currentRoom != null)
        {
            loadMessages();
        }
    }

    private void configureLayout()
    {
        addClassName("chat-view");

        setSizeFull();

        setPadding(false);

        setSpacing(false);

        messageList.setWidthFull();
        messageList.setPadding(true);
        messageList.setSpacing(false);
        messageList.addClassName("message-list");

        messageScroller.setSizeFull();
        messageScroller.addClassName("message-scroller");

        messageField.setPlaceholder("Type a message...");
        messageField.setWidthFull();
        messageField.addClassName("message-field");

        sendButton.setText("Send");
        sendButton.addClassName("primary-button");

        HorizontalLayout inputLayout = new HorizontalLayout(messageField, sendButton);

        inputLayout.setWidthFull();
        inputLayout.setPadding(true);
        inputLayout.setAlignItems(FlexComponent.Alignment.END);
        inputLayout.addClassName("input-layout");

        sendButton.addClickListener(e -> sendMessage());

        add(messageScroller, inputLayout);

        expand(messageScroller);
    }

    private void loadMessages()
    {
        messageList.removeAll();

        if (currentRoom == null)
        {
            messageList.add(new Paragraph("Chat room not found."));
            return;
        }

        List<Message> messages = messageService.findByChatRoomId(currentRoom.getId());

        for (Message message : messages)
        {
            messageList.add(createMessageBubble(message));
        }

        messageScroller.getElement().executeJs("this.scrollTop = this.scrollHeight;");
    }

    private HorizontalLayout createMessageBubble(Message message)
    {
        HorizontalLayout bubble = new HorizontalLayout();

        bubble.setWidthFull();
        bubble.setPadding(false);
        bubble.setSpacing(true);
        bubble.setAlignItems(FlexComponent.Alignment.CENTER);
        bubble.setJustifyContentMode(JustifyContentMode.BETWEEN);

        User currentUser = SessionUtils.getUser();

        boolean isOwnMessage = currentUser != null && message.getSender() != null &&
                currentUser.getId().equals(message.getSender().getId());

        HorizontalLayout leftSide = new HorizontalLayout();

        leftSide.setAlignItems(FlexComponent.Alignment.BASELINE);
        leftSide.setSpacing(true);

        Div nickname = new Div();

        nickname.setText(message.getSender() != null ? message.getSender().getUsername() : "Unknown");
        nickname.addClassName("chat-nickname");

        if (isOwnMessage)
        {
            nickname.addClassName("chat-nickname-self");
        }

        Div messageContent = new Div();

        messageContent.setText(message.getContent() != null ? message.getContent() : "");
        messageContent.addClassName("chat-message-content");

        leftSide.add(nickname, messageContent);

        Div timestamp = new Div();

        timestamp.setText(formatTimestamp(message.getTimestamp()));
        timestamp.addClassName("chat-timestamp");

        bubble.add(leftSide, timestamp);

        return bubble;
    }

    private String formatTimestamp(LocalDateTime timestamp)
    {
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(timestamp, now);

        if (duration.toMinutes() < 15)
        {
            long minutesAgo = Math.max(duration.toMinutes(), 1);

            return minutesAgo + " minutes ago";
        }
        else if (duration.toHours() < 24)
        {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            return timestamp.format(timeFormatter);
        }
        else
        {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return timestamp.format(dateFormatter);
        }
    }

    private void sendMessage()
    {
        String content = messageField.getValue();

        User sender = SessionUtils.getUser();

        if (content != null && !content.trim().isEmpty() && sender != null && currentRoom != null)
        {
            Message message = new Message();
            message.setSender(sender);
            message.setChatRoom(currentRoom);
            message.setContent(content);
            message.setTimestamp(LocalDateTime.now());

            messageService.save(message);

            messageField.clear();
            messageField.focus();

            loadMessages(); // own UI
            Broadcaster.broadcast(); // tell others to reload
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        Optional<String> chatRoomId = event.getRouteParameters().get("chatRoomId");

        if (chatRoomId.isPresent())
        {
            try
            {
                Long id = Long.parseLong(chatRoomId.get());

                this.currentRoom = chatRoomService.findById(id).orElse(null);

                loadMessages();
            }
            catch (NumberFormatException e)
            {
                messageList.add(new Paragraph("Invalid room ID."));
            }
        }
        else
        {
            messageList.add(new Paragraph("No room selected."));
        }
    }
}
