package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.Message;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.MessageService;
import online.robodoc.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Route("messages")
public class MessageView extends VerticalLayout
{

    private final MessageService messageService;

    private final UserService userService;

    private final ChatRoomService chatRoomService;

    private final Grid<Message> grid = new Grid<>(Message.class, false);

    private final TextField senderUsernameField = new TextField("Sender Username");
    private final TextField chatRoomIdField = new TextField("ChatRoom ID");
    private final TextField contentField = new TextField("Message Content");

    private final Button sendButton = new Button("Send Message");

    private final TextField filterContentField = new TextField("Filter by Content");
    private final TextField filterSenderField = new TextField("Filter by Sender Username");
    private final TextField filterChatRoomField = new TextField("Filter by ChatRoom Name");
    private final TextField filterIdField = new TextField("Filter by Message ID");
    private final TextField filterTimestampField = new TextField("Filter by Timestamp Contains");

    private final Button applyFiltersButton = new Button("Apply Filters");

    @Autowired
    public MessageView(MessageService messageService, UserService userService, ChatRoomService chatRoomService)
    {
        this.messageService = messageService;

        this.userService = userService;

        this.chatRoomService = chatRoomService;

        configureGrid();
        configureForm();

        add(
                new HorizontalLayout(senderUsernameField, chatRoomIdField, contentField, sendButton),
                new HorizontalLayout(filterContentField, filterSenderField, filterChatRoomField,
                        filterIdField, filterTimestampField, applyFiltersButton),
                grid
        );

        updateGrid();
    }

    private void configureGrid()
    {
        grid.addColumn(Message::getId).setHeader("ID");
        grid.addColumn(Message::getContent).setHeader("Content");
        grid.addColumn(Message::getTimestamp).setHeader("Timestamp");
        grid.addColumn(message ->
        {
            if (message.getSender() != null)
            {
                return message.getSender().getUsername();
            }
            else
            {
                return "-";
            }
        }).setHeader("Sender");

        grid.addColumn(message ->
        {
            if (message.getChatRoom() != null)
            {
                return message.getChatRoom().getName();
            }
            else
            {
                return "-";
            }
        }).setHeader("ChatRoom");
    }

    private void configureForm()
    {
        sendButton.addClickListener(e ->
        {
            String senderUsername = senderUsernameField.getValue();
            String chatRoomIdStr = chatRoomIdField.getValue();
            String content = contentField.getValue();

            if (!senderUsername.isEmpty() && !chatRoomIdStr.isEmpty() && !content.isEmpty())
            {
                try
                {
                    Long chatRoomId = Long.parseLong(chatRoomIdStr);
                    User sender = userService.findByUsername(senderUsername);
                    ChatRoom chatRoom = chatRoomService.findById(chatRoomId).orElse(null);

                    if (sender != null && chatRoom != null)
                    {
                        Message message = new Message();
                        message.setSender(sender);
                        message.setChatRoom(chatRoom);
                        message.setContent(content);
                        message.setTimestamp(LocalDateTime.now());

                        messageService.save(message);

                        senderUsernameField.clear();
                        chatRoomIdField.clear();
                        contentField.clear();

                        updateGrid();
                        Notification.show("Message sent");
                    }
                    else
                    {
                        Notification.show("Sender or ChatRoom not found");
                    }
                }
                catch (NumberFormatException ex)
                {
                    Notification.show("Invalid ChatRoom ID");
                }
            }
        });

        applyFiltersButton.addClickListener(e ->
        {
            List<Message> allMessages = messageService.findAll();
            List<Message> filtered = allMessages.stream()
                    .filter(msg ->
                    {
                        boolean matches = true;

                        String contentFilter = filterContentField.getValue();

                        if (contentFilter != null && !contentFilter.isEmpty())
                        {
                            matches &= msg.getContent() != null && msg.getContent()
                                    .toLowerCase()
                                    .contains(contentFilter.toLowerCase());
                        }

                        String senderFilter = filterSenderField.getValue();

                        if (senderFilter != null && !senderFilter.isEmpty())
                        {
                            matches &= msg.getSender() != null && msg.getSender()
                                    .getUsername()
                                    .toLowerCase()
                                    .contains(senderFilter.toLowerCase());
                        }

                        String chatRoomFilter = filterChatRoomField.getValue();

                        if (chatRoomFilter != null && !chatRoomFilter.isEmpty())
                        {
                            matches &= msg.getChatRoom() != null && msg.getChatRoom()
                                    .getName()
                                    .toLowerCase()
                                    .contains(chatRoomFilter.toLowerCase());
                        }

                        String idFilter = filterIdField.getValue();

                        if (idFilter != null && !idFilter.isEmpty())
                        {
                            try
                            {
                                Long id = Long.parseLong(idFilter);
                                matches &= msg.getId().equals(id);
                            }
                            catch (NumberFormatException ignored)
                            {

                            }
                        }

                        String timestampFilter = filterTimestampField.getValue();

                        if (timestampFilter != null && !timestampFilter.isEmpty())
                        {
                            matches &= msg.getTimestamp() != null && msg.getTimestamp()
                                    .toString()
                                    .contains(timestampFilter);
                        }

                        return matches;
                    })
                    .collect(Collectors.toList());

            grid.setItems(filtered);
        });
    }

    private void updateGrid()
    {
        grid.setItems(messageService.findAll());
    }
}
