package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("chatrooms")
public class ChatRoomView extends VerticalLayout {

    private final ChatRoomService chatRoomService;

    private final UserService userService;

    private final Grid<ChatRoom> grid = new Grid<>(ChatRoom.class, false);

    private final TextField roomNameField = new TextField("Room Name");
    private final TextField descriptionField = new TextField("Description");
    private final TextField creatorUsernameField = new TextField("Creator Username");

    private final Button addButton = new Button("Add Room");

    @Autowired
    public ChatRoomView(ChatRoomService chatRoomService, UserService userService)
    {
        this.chatRoomService = chatRoomService;

        this.userService = userService;

        configureGrid();

        configureForm();

        add(new HorizontalLayout(roomNameField, descriptionField, creatorUsernameField, addButton), grid);

        updateGrid();
    }

    private void configureGrid()
    {
        grid.addColumn(ChatRoom::getId).setHeader("ID");
        grid.addColumn(ChatRoom::getName).setHeader("Name");
        grid.addColumn(ChatRoom::getDescription).setHeader("Description");
        grid.addColumn(chatRoom -> {
            if (chatRoom.getCreator() != null)
            {
                return chatRoom.getCreator().getUsername();
            }
            else
            {
                return "-";
            }
        }).setHeader("Creator");

        grid.addComponentColumn(chatRoom ->
        {
            Button deleteButton = new Button("Delete", event ->
            {
                String username = creatorUsernameField.getValue();

                User user = userService.findByUsername(username);

                if (user != null)
                {
                    try
                    {
                        chatRoomService.delete(chatRoom, user);
                        updateGrid();
                        Notification.show("Deleted");
                    }
                    catch (RuntimeException ex)
                    {
                        Notification.show("Unauthorized to delete");
                    }
                }
                else
                {
                    Notification.show("Invalid user");
                }
            });
            return deleteButton;
        });
    }

    private void configureForm()
    {
        addButton.addClickListener(e ->
        {
            String roomName = roomNameField.getValue();
            String description = descriptionField.getValue();
            String creatorUsername = creatorUsernameField.getValue();

            if (!roomName.isEmpty() && !creatorUsername.isEmpty())
            {
                User creator = userService.findByUsername(creatorUsername);

                if (creator != null)
                {
                    ChatRoom room = new ChatRoom();

                    room.setName(roomName);
                    room.setDescription(description);
                    room.setCreator(creator);

                    chatRoomService.save(room);

                    roomNameField.clear();
                    descriptionField.clear();
                    creatorUsernameField.clear();

                    updateGrid();
                }
                else
                {
                    Notification.show("Creator user not found!");
                }
            }
        });
    }

    private void updateGrid()
    {
        grid.setItems(chatRoomService.findAll());
    }
}
