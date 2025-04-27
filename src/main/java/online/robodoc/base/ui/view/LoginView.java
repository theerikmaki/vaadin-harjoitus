package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.ChatRoomService;
import online.robodoc.base.service.UserService;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "login")
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends VerticalLayout
{
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public LoginView(UserService userService, ChatRoomService chatRoomService)
    {
        this.userService = userService;

        this.chatRoomService = chatRoomService;

        configureLayout();
    }

    private void configureLayout()
    {
        setSizeFull();

        setAlignItems(FlexComponent.Alignment.CENTER);

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H2 title = new H2("Login to Chat");

        TextField usernameField = new TextField("Username");

        PasswordField passwordField = new PasswordField("Password");

        Button loginButton = new Button("Login");

        add(title, usernameField, passwordField, loginButton);

        loginButton.addClickListener(event ->
        {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty())
            {
                Notification.show("Please fill both fields.");

                return;
            }

            User user = userService.findByUsername(username);

            if (user != null && user.getPassword().equals(password))
            {
                SessionUtils.setUser(user);

                if (!chatRoomService.findAll().isEmpty())
                {
                    Long firstRoomId = chatRoomService.findAll().get(0).getId();

                    getUI().ifPresent(ui -> ui.navigate("chat/" + firstRoomId));
                }
                else
                {
                    Notification.show("No chat rooms available.");
                }

            }
            else
            {
                Notification.show("Invalid username or password.");
            }
        });
    }
}
