package online.robodoc.base.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import online.robodoc.base.domain.User;
import online.robodoc.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.checkbox.Checkbox;

@Route("users")
public class UserView extends VerticalLayout
{
    private final UserService userService;

    private final Grid<User> grid = new Grid<>(User.class, false);

    private final TextField usernameField = new TextField("Username");
    private final TextField passwordField = new TextField("Password");

    private final Button addButton = new Button("Add User");

    @Autowired
    public UserView(UserService userService)
    {
        this.userService = userService;

        configureGrid();

        configureForm();

        add(usernameField, passwordField, addButton, grid);

        updateGrid();
    }

    private void configureGrid() {
        grid.addColumn(User::getId).setHeader("ID");

        Grid.Column<User> usernameColumn = grid.addColumn(User::getUsername).setHeader("Username");

        Grid.Column<User> adminColumn = grid.addColumn(user -> user.getisAdmin() ? "True" : "False").setHeader("Admin");

        Binder<User> binder = new Binder<>(User.class);

        Editor<User> editor = grid.getEditor();

        editor.setBinder(binder);

        editor.setBuffered(true);

        TextField usernameEditor = new TextField();

        binder.bind(usernameEditor, User::getUsername, User::setUsername);

        usernameColumn.setEditorComponent(usernameEditor);

        Checkbox adminEditor = new Checkbox();

        binder.bind(adminEditor, User::getisAdmin, User::setAdmin);

        adminColumn.setEditorComponent(adminEditor);

        Button saveButton = new Button("Save", e -> {
            editor.save();
        });
        Button cancelButton = new Button("Cancel", e -> {
            editor.cancel();
        });

        grid.addComponentColumn(user -> {
            Button editButton = new Button("Edit", event -> {
                editor.editItem(user);

                usernameEditor.focus();
            });

            return new HorizontalLayout(editButton, saveButton, cancelButton);
        });

        editor.addSaveListener(event -> {
            userService.save(event.getItem());

            updateGrid();
        });
    }

    private void configureForm()
    {
        addButton.addClickListener(e ->
        {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (!username.isEmpty() && !password.isEmpty())
            {
                User user = new User();

                user.setUsername(username);
                user.setPassword(password);
                user.setAdmin(false);

                userService.save(user);

                usernameField.clear();
                passwordField.clear();

                updateGrid();
            }
        });
    }

    private void updateGrid()
    {
        grid.setItems(userService.findAll());
    }
}
