package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
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

        addClassName("login-view");

        setAlignItems(FlexComponent.Alignment.CENTER);

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Div form = new Div();

        form.addClassName("login-form");

        H2 title = new H2("Login to Chat");

        title.addClassName("login-title");

        TextField usernameField = new TextField("Username");

        usernameField.addClassName("login-input");

        PasswordField passwordField = new PasswordField("Password");

        passwordField.addClassName("login-input");

        Button loginButton = new Button("Login");

        loginButton.addClassName("login-button");

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

                SessionUtils.setUser(user);
                getUI().ifPresent(ui -> {
                    MainLayout mainLayout = (MainLayout) ui.getChildren()
                            .filter(c -> c instanceof MainLayout)
                            .findFirst()
                            .orElse(null);

                    if (mainLayout != null)
                    {
                        mainLayout.refreshRooms();
                    }

                    ui.navigate("chat/1");
                });

            }
            else
            {
                Notification.show("Invalid username or password.");
            }
        });

        form.add(title, usernameField, passwordField, loginButton);

        add(form);
    }
}