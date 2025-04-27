package online.robodoc.base.ui.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.User;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile")
public class ProfileView extends VerticalLayout
{

    public ProfileView()
    {
        if (!SessionUtils.isLoggedIn())
        {
            getUI().ifPresent(ui -> ui.navigate(""));
            return;
        }

        User user = SessionUtils.getUser();

        H2 title = new H2("User Profile");

        Paragraph username = new Paragraph("Username: " + user.getUsername());

        Paragraph role = new Paragraph("Role: " + (user.getisAdmin() ? "Admin" : "User"));

        add(title, username, role);

        setAlignItems(Alignment.CENTER);
    }
}
