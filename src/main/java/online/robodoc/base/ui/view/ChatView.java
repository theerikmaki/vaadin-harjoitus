package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.Message;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.MessageService;
import online.robodoc.base.service.UserService;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;


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
    private final TextField messageField = new TextField();
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
            getUI().ifPresent(ui -> ui.navigate(""));
            return;
        }

        configureLayout();
    }

    private void configureLayout()
    {
        setSizeFull();

        setPadding(false);
        setSpacing(false);

        messageList.setWidthFull();
        messageList.setPadding(false);
        messageList.setSpacing(false);
        messageList.getStyle()
                .set("gap", "0px")
                .set("padding", "0px")
                .set("margin", "0px");


        messageScroller.setSizeFull();

        messageField.setPlaceholder("Type a message...");
        messageField.setWidthFull();

        messageField.addKeyPressListener(Key.ENTER, event -> {
            if (!event.getKey().getKeys().contains("Shift")) {
                sendButton.click();
            }
        });

        sendButton.addClickListener(e -> sendMessage());

        HorizontalLayout inputLayout = new HorizontalLayout(messageField, sendButton);

        inputLayout.setWidthFull();
        inputLayout.setPadding(true);
        inputLayout.setAlignItems(FlexComponent.Alignment.END);

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

    private String formatTimestamp(LocalDateTime timestamp) {
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


    private HorizontalLayout createMessageBubble(Message message) {
        HorizontalLayout bubble = new HorizontalLayout();

        bubble.setWidthFull();
        bubble.setPadding(false);
        bubble.setSpacing(true);
        bubble.getStyle()
                .set("background-color", "#f3f4f6")
                .set("border-radius", "var(--lumo-border-radius-s)")
                .set("padding", "var(--lumo-space-xs)")
                .set("margin", "0")
                .set("margin-bottom", "2px")
                .set("align-items", "baseline");

        String senderName = message.getSender() != null ? message.getSender().getUsername() : "Unknown";

        String content = message.getContent() != null ? message.getContent() : "";

        String timeText = message.getTimestamp() != null ? formatTimestamp(message.getTimestamp()) : "";

        Div nickname = new Div();

        nickname.setText(senderName);

        nickname.getStyle().set("font-weight", "bold").set("margin-right", "var(--lumo-space-s)");

        Div messageContent = new Div();

        messageContent.setText(content);

        messageContent.getStyle()
                .set("flex-grow", "1")
                .set("white-space", "pre-wrap")
                .set("word-break", "break-word")
                .set("overflow-wrap", "break-word");

        Div timestamp = new Div();

        timestamp.setText(timeText);

        timestamp.getStyle()
                .set("font-size", "var(--lumo-font-size-xs)")
                .set("color", "gray")
                .set("margin-left", "auto"); // push it to the far right

        bubble.add(nickname, messageContent, timestamp);

        return bubble;
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

            loadMessages();
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
