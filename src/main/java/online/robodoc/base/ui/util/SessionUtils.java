package online.robodoc.base.ui.util;

import com.vaadin.flow.server.VaadinSession;
import online.robodoc.base.domain.User;

public class SessionUtils {

    private static final String USER_SESSION_KEY = "loggedUser";

    public static void setUser(User user)
    {
        VaadinSession.getCurrent().setAttribute(USER_SESSION_KEY, user);
    }

    public static User getUser()
    {
        return (User) VaadinSession.getCurrent().getAttribute(USER_SESSION_KEY);
    }

    public static boolean isLoggedIn()
    {
        return getUser() != null;
    }
}
