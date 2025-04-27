package online.robodoc.base.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.ui.util.SessionUtils;
import online.robodoc.base.ui.view.AboutView;
import online.robodoc.base.ui.view.ChatView;
import online.robodoc.base.ui.view.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;


import java.util.List;

import static online.robodoc.base.ui.util.SessionUtils.getUser;

@Layout
@CssImport("./themes/default/styles.css")
public class MainLayout extends AppLayout
{
    private final ChatRoomService chatRoomService;

    private final VerticalLayout navLayout = new VerticalLayout();

    private Button createRoomButton;

    @Autowired
    public MainLayout(ChatRoomService chatRoomService)
    {
        this.chatRoomService = chatRoomService;

        addToDrawer(navLayout);

        createHeader();
        createDrawer();
        createFooter();
    }

    private void createHeader()
    {
        H2 logo = new H2("Chatty Chat");

        logo.addClassName("app-logo");

        Button logoutButton = new Button("Logout", event ->
        {
            SessionUtils.setUser(null);

            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        logoutButton.addClassName("logout-button");

        HorizontalLayout header = new HorizontalLayout(logo, logoutButton);

        header.setWidthFull();

        header.setAlignItems(FlexComponent.Alignment.CENTER);

        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        header.addClassName("app-header");

        addToNavbar(header);
    }

    private VerticalLayout roomsLayout;

    private void createDrawer()
    {
        navLayout.setPadding(false);
        navLayout.setSpacing(false);
        navLayout.setWidthFull();
        navLayout.addClassName("nav-layout");

        RouterLink profileLink = new RouterLink("Profile", ProfileView.class);
        RouterLink aboutLink = new RouterLink("About", AboutView.class);

        profileLink.addClassName("nav-button");
        aboutLink.addClassName("nav-button");

        Div roomsHeader = new Div();

        roomsHeader.setText("Chat Rooms");
        roomsHeader.addClassName("chat-rooms-header");

        roomsLayout = new VerticalLayout();
        roomsLayout.setPadding(false);
        roomsLayout.setSpacing(false);
        roomsLayout.setWidthFull();
        roomsLayout.addClassName("rooms-layout");

        createRoomButton = new Button("Create Room");
        createRoomButton.addClassName("create-room-button");
        createRoomButton.addClickListener(e -> openCreateRoomDialog());

        roomsLayout.add(createRoomButton);

        navLayout.add(profileLink, aboutLink, roomsHeader, roomsLayout);

        refreshRooms();

        addToDrawer(navLayout);
    }

    public void refreshRooms()
    {
        roomsLayout.getChildren()
                .filter(component -> component != createRoomButton)
                .forEach(component -> roomsLayout.remove(component));

        if (SessionUtils.isLoggedIn())
        {
            try
            {
                List<ChatRoom> rooms = chatRoomService.findAll();

                if (rooms != null && !rooms.isEmpty())
                {
                    for (ChatRoom room : rooms)
                    {
                        HorizontalLayout roomLayout = new HorizontalLayout();

                        roomLayout.setWidthFull();
                        roomLayout.setAlignItems(FlexComponent.Alignment.CENTER);

                        RouterLink roomLink = new RouterLink();

                        roomLink.setText(room.getName());
                        roomLink.getElement().setAttribute("href", "chat/" + room.getId()); // manual href
                        roomLink.addClassName("chat-room-link");

                        roomLayout.add(roomLink);

                        if (room.getOwner().getId().equals(getUser().getId())) {
                            Button deleteButton = new Button("Delete");
                            deleteButton.addClassName("delete-room-button");
                            deleteButton.addClickListener(e -> openDeleteConfirmDialog(room, getUser()));
                            roomLayout.add(deleteButton);
                        }

                        roomsLayout.add(roomLayout);
                    }
                }
                else
                {
                    Div noRooms = new Div();
                    noRooms.setText("No chat rooms yet.");
                    roomsLayout.add(noRooms);
                }
            }
            catch (Exception e)
            {
                Div error = new Div();
                error.setText("Error loading rooms: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                roomsLayout.add(error);
            }
        }
        else
        {
            Div pleaseLogin = new Div();
            pleaseLogin.setText("Please log in to see rooms.");
            roomsLayout.add(pleaseLogin);
        }
    }

    private void createFooter()
    {
        Footer footer = new Footer();
        footer.add(new Span("© Erik Mäki 2025"));
        addToDrawer(footer);
    }

    private void openDeleteConfirmDialog(ChatRoom room, User user)
    {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm Deletion");
        dialog.setText("Are you sure you want to delete room \"" + room.getName() + "\"?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event ->
        {
            chatRoomService.delete(room, user);
            refreshRooms();
        });

        dialog.open();
    }


    private void openCreateRoomDialog()
    {
        Dialog dialog = new Dialog();

        VerticalLayout layout = new VerticalLayout();

        TextField roomNameField = new TextField("Room Name");

        Button createButton = new Button("Create", e ->
        {
            if (!roomNameField.getValue().trim().isEmpty())
            {
                ChatRoom newRoom = new ChatRoom();

                newRoom.setName(roomNameField.getValue().trim());
                newRoom.setOwner(getUser());

                chatRoomService.save(newRoom);

                chatRoomService.save(newRoom);

                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> {
                        refreshRooms();
                    });
                }

                dialog.close();


                dialog.close();
            }
        });

        layout.add(roomNameField, createButton);

        dialog.add(layout);

        dialog.open();
    }

}
