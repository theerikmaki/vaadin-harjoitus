package online.robodoc.base.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.ui.util.SessionUtils;
import online.robodoc.base.ui.view.AboutView;
import online.robodoc.base.ui.view.ChatView;
import online.robodoc.base.ui.view.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Layout
@CssImport("./themes/default/styles.css")
public class MainLayout extends AppLayout
{
    private final ChatRoomService chatRoomService;

    private final VerticalLayout navLayout = new VerticalLayout();

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
        navLayout.addClassName("nav-layout"); // <- correct holy class

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

        navLayout.add(profileLink, aboutLink, roomsHeader, roomsLayout);

        refreshRooms();

        addToDrawer(navLayout);
    }


    public void refreshRooms()
    {
        roomsLayout.removeAll();

        if (SessionUtils.isLoggedIn())
        {
            try
            {
                List<ChatRoom> rooms = chatRoomService.findAll();

                if (rooms != null && !rooms.isEmpty())
                {
                    for (ChatRoom room : rooms)
                    {
                        Anchor roomLink = new Anchor("chat/" + room.getId(), room.getName());

                        roomLink.addClassName("chat-room-link");

                        roomsLayout.add(roomLink);
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

                error.setText("Error loading rooms.");

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
}
