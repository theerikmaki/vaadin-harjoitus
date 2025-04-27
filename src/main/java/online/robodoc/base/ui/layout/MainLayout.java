package online.robodoc.base.ui.layout;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.User;
import online.robodoc.base.ui.view.ChatView;
import online.robodoc.base.ui.view.ProfileView;
import online.robodoc.base.ui.view.AboutView;
import online.robodoc.base.ui.view.LoginView;

@Layout
public class MainLayout extends AppLayout
{

    public MainLayout()
    {
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

        RouterLink chatLink = new RouterLink("Chat", ChatView.class);
        RouterLink profileLink = new RouterLink("Profile", ProfileView.class);
        RouterLink aboutLink = new RouterLink("About", AboutView.class);

        nav.add(chatLink, profileLink, aboutLink);
        addToDrawer(nav);
    }

    private void createFooter()
    {
        Footer footer = new Footer();
        footer.add(new Span("© Erik Mäki 2025"));
        addToDrawer(footer);
    }
}
