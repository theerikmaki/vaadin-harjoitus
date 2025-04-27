package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.UserService;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends VerticalLayout
{

    private final UserService userService;

    @Autowired
    public LoginView(UserService userService)
    {
        this.userService = userService;

        H2 title = new H2("Login");

        TextField username = new TextField("Username");

        PasswordField password = new PasswordField("Password");

        Button loginButton = new Button("Login");

        add(title, username, password, loginButton);

        setAlignItems(Alignment.CENTER);

        loginButton.addClickListener(event ->
        {
            User user = userService.findByUsername(username.getValue());

            if (user != null && user.getPassword().equals(password.getValue()))
            {
                SessionUtils.setUser(user);

                getUI().ifPresent(ui -> ui.navigate("chat"));
            }
            else
            {
                Notification.show("Invalid credentials");
            }
        });
    }
}
