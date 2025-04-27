package online.robodoc.base.ui.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "about", layout = online.robodoc.base.ui.layout.MainLayout.class)
@PageTitle("About | Chatty Chat")
public class AboutView extends VerticalLayout
{

    public AboutView()
    {
        addClassName("about-view");

        H1 title = new H1("About Chatty Chat");

        title.addClassName("about-title");

        Paragraph description = new Paragraph(
                "Chatty Chat is barebones and simple chat application made as a course project"
        );

        description.addClassName("about-description");

        add(title, description);
    }
}
