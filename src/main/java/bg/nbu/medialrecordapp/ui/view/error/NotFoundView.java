package bg.nbu.medialrecordapp.ui.view.error;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("404")
@PageTitle("Page Not Found")
@PermitAll
public class NotFoundView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";

    public NotFoundView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);

        Button goBack = new Button("← Go Back", _ -> getUI().ifPresent(ui -> ui.getPage().getHistory().back()));
        goBack.getStyle().set("color", TITLE_COLOR);
        add(goBack);

        H1 title = new H1("404 - Page Not Found");
        title.getStyle().set("color", TITLE_COLOR);
        Paragraph description = new Paragraph("The page you are looking for does not exist.");
        description.getStyle().set("color", SUBTITLE_COLOR);
        add(title, description);
    }
}