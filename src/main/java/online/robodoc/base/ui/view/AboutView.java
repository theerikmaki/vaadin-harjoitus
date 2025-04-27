package online.robodoc.base.ui.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import online.robodoc.base.ui.layout.MainLayout;
import online.robodoc.base.ui.util.SessionUtils;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends VerticalLayout
{

    public AboutView()
    {
        if (!SessionUtils.isLoggedIn())
        {
            getUI().ifPresent(ui -> ui.navigate(""));

            return;
        }

        H2 title = new H2("About This Application");

        Paragraph description = new Paragraph(
                "A simplistic chat application made as a course project"
        );

        add(title, description);

        setAlignItems(Alignment.CENTER);
    }
}
