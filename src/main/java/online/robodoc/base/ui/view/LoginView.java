package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class LoginView extends VerticalLayout
{

    private final UserService userService;

    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");

    private final Button loginButton = new Button("Login");

    @Autowired
    public LoginView(UserService userService)
    {
        this.userService = userService;

        add(usernameField, passwordField, loginButton);

        loginButton.addClickListener(event ->
        {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty())
            {
                Notification.show("Please enter username and password");
                return;
            }

            User user = userService.findByUsername(username);

            if (user != null && user.getPassword().equals(password))
            {
                getUI().ifPresent(ui -> ui.navigate("messages"));
            }
            else
            {
                Notification.show("Invalid username or password");
            }
        });
    }
}
