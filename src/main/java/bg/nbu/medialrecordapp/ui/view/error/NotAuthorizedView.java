package bg.nbu.medialrecordapp.ui.view.error;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("403")
@PageTitle("Not Authorized")
@PermitAll
public class NotAuthorizedView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";

    public NotAuthorizedView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);

        Button goBack = new Button("← Go Back", _ -> getUI().ifPresent(ui -> ui.getPage().getHistory().back()));
        goBack.getStyle().set("color", TITLE_COLOR);
        add(goBack);

        H1 title = new H1("403 - Not Authorized");
        title.getStyle().set("color", TITLE_COLOR);
        Paragraph description = new Paragraph("You do not have permission to access this page.");
        description.getStyle().set("color", SUBTITLE_COLOR);
        add(title, description);
    }
}