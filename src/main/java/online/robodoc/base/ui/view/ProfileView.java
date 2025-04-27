package online.robodoc.base.ui.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.User;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;

@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile | Chatty Chat")
public class ProfileView extends VerticalLayout
{

    public ProfileView()
    {
        addClassName("profile-view");

        User user = SessionUtils.getUser();

        if (user == null)
        {
            getUI().ifPresent(ui -> ui.navigate("login"));

            return;
        }

        H2 title = new H2("Your Profile");

        title.addClassName("profile-title");

        Div userInfo = new Div();

        userInfo.addClassName("profile-info");

        Paragraph username = new Paragraph("Username: " + user.getUsername());

        username.addClassName("profile-field");

        Paragraph admin = new Paragraph("Admin: " + (user.getisAdmin() ? "Yes" : "No"));

        admin.addClassName("profile-field");

        userInfo.add(username, admin);

        add(title, userInfo);
    }
}
