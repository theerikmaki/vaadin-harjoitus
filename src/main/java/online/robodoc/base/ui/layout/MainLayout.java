package online.robodoc.base.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
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
        H1 title = new H1("Chatty Chat");

        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        Button logoutButton = new Button("Logout", e ->
        {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();

            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout header = new HorizontalLayout(title, logoutButton);

        header.setWidthFull();
        header.expand(title);
        header.setPadding(true);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer()
    {
        navLayout.removeAll();

        navLayout.setPadding(true);
        navLayout.setSpacing(true);
        navLayout.setAlignItems(FlexComponent.Alignment.START);

        navLayout.add(new RouterLink("Profile", ProfileView.class));
        navLayout.add(new RouterLink("About", AboutView.class));

        Div roomsHeader = new Div();

        roomsHeader.setText("Chat Rooms");
        roomsHeader.getStyle()
                .set("font-weight", "bold")
                .set("margin-top", "1rem");

        navLayout.add(roomsHeader);

        try
        {
            if (SessionUtils.isLoggedIn())
            {
                List<ChatRoom> rooms = chatRoomService.findAll();

                if (rooms != null && !rooms.isEmpty())
                {
                    for (ChatRoom room : rooms)
                    {
                        RouterLink roomLink = new RouterLink();

                        roomLink.setText(room.getName());
                        roomLink.getElement().setAttribute("href", "chat/" + room.getId());

                        navLayout.add(roomLink);
                    }
                }
                else
                {
                    Div noRooms = new Div();

                    noRooms.setText("No chat rooms yet.");

                    navLayout.add(noRooms);
                }
            }
            else
            {
                Div notLoggedIn = new Div();

                notLoggedIn.setText("Please log in to see rooms.");

                navLayout.add(notLoggedIn);
            }
        }
        catch (Exception ex)
        {
            Div error = new Div();

            error.setText("Error loading rooms.");

            navLayout.add(error);
        }
    }

    public void refreshDrawer()
    {
        createDrawer();
    }

    private void createFooter()
    {
        Footer footer = new Footer();
        footer.add(new Span("© Erik Mäki 2025"));
        addToDrawer(footer);
    }
}
