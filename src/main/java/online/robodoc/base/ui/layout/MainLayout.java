package online.robodoc.base.ui.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.ChatRoom;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.ui.util.SessionUtils;
import online.robodoc.base.ui.view.AboutView;
import online.robodoc.base.ui.view.ProfileView;

import java.util.List;

@Layout
public class MainLayout extends AppLayout
{

    private final ChatRoomService chatRoomService;

    public MainLayout(ChatRoomService chatRoomService)
    {
        this.chatRoomService = chatRoomService;

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
        Nav nav = new Nav();

        RouterLink profileLink = new RouterLink("Profile", ProfileView.class);
        RouterLink aboutLink = new RouterLink("About", AboutView.class);

        nav.add(profileLink, aboutLink);

        if (SessionUtils.isLoggedIn())
        {
            List<ChatRoom> rooms = chatRoomService.findAll();

            for (ChatRoom room : rooms)
            {
                RouterLink roomLink = new RouterLink();
                roomLink.setText(room.getName());
                roomLink.setRoute(online.robodoc.base.ui.view.ChatView.class);
                roomLink.getElement().setAttribute("href", "chat/" + room.getId());

                nav.add(roomLink);
            }
        }

        addToDrawer(nav);
    }

    private void createFooter() {
        Footer footer = new Footer();
        footer.add(new Span("© Erik Mäki 2025"));
        addToDrawer(footer);
    }
}
