package bg.nbu.medialrecordapp.ui.view.home;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("my-health")
@RolesAllowed("PATIENT")
public class HomeView extends VerticalLayout {

    private final static String TITLE_COLOR = "#14532D";
    private final static String SUBTITLE_COLOR = "#4B7560";
    private final static String BACKGROUND_COLOR = "#DCFCE7";
    private final static String GLOW_COLOR = "rgba(34, 197, 75, 0.18)";
    private final static String BORDER_COLOR = "#BBF7D0";
    private final static String ICON_COLOR = "#16A34A";

    public HomeView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(FlexComponent.Alignment.CENTER);
        getStyle()
                .set("background", "linear-gradient(180deg, #F7FFF9 0%, #ECFDF3 100%)")
                .set("min-height", "100vh");

        VerticalLayout page = new VerticalLayout();
        page.setWidthFull();
        page.setMaxWidth("1200px");
        page.setPadding(true);
        page.setSpacing(true);
        page.setAlignItems(FlexComponent.Alignment.CENTER);
        page.getStyle().set("padding", "32px");

        Div hero = createHeroSection();
        hero.setWidthFull();

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setSpacing(true);
        cards.setPadding(false);
        cards.setAlignItems(FlexComponent.Alignment.STRETCH);

        cards.add(
                createInfoCard(
                        VaadinIcon.DOCTOR,
                        "My GP",
                        "No GP assigned yet",
                        "Your general practitioner information will appear here."
                ),
                createInfoCard(
                        VaadinIcon.CLIPBOARD_TEXT,
                        "Examinations",
                        "View medical examinations",
                        "Check your previous and upcoming examinations."
                ),
                createInfoCard(
                        VaadinIcon.FILE_TEXT,
                        "Medical Records",
                        "Health documents",
                        "Access your medical history and health information."
                )
        );

        Button examinationsButton = new Button(
                "View Examinations",
                new Icon(VaadinIcon.ARROW_RIGHT),
                event -> getUI().ifPresent(ui -> ui.navigate("examinations"))
        );
        examinationsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        examinationsButton.getStyle()
                .set("margin-top", "8px")
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR);

        page.add(hero, cards, examinationsButton);
        add(page);
    }

    private Div createHeroSection() {
        Div hero = new Div();
        hero.setWidthFull();
        hero.getStyle()
                .set("background", "white")
                .set("border-radius", "24px")
                .set("padding", "32px")
                .set("box-shadow", "0 8px 24px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR);

        H1 title = new H1("My Health Information");
        title.getStyle()
                .set("margin", "16px 0 8px 0")
                .set("font-size", "36px")
                .set("color", TITLE_COLOR);

        Paragraph subtitle = new Paragraph(
                "Welcome to your personal health dashboard."
        );
        subtitle.getStyle()
                .set("max-width", "720px")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "16px")
                .set("line-height", "1.6")
                .set("margin", "0");

        hero.add(title, subtitle);
        return hero;
    }

    private Div createInfoCard(VaadinIcon iconType, String title, String value, String description) {
        Div card = new Div();
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("padding", "24px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("flex", "1");

        Icon icon = new Icon(iconType);
        icon.getStyle()
                .set("width", "32px")
                .set("height", "32px")
                .set("color", ICON_COLOR)
                .set("background", BACKGROUND_COLOR)
                .set("border-radius", "12px")
                .set("padding", "10px");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle()
                .set("font-size", "18px")
                .set("margin", "16px 0 6px 0")
                .set("color", TITLE_COLOR);

        Span cardValue = new Span(value);
        cardValue.getStyle()
                .set("display", "block")
                .set("font-weight", "600")
                .set("color", TITLE_COLOR)
                .set("margin-bottom", "8px");

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.getStyle()
                .set("margin", "0")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "14px")
                .set("line-height", "1.5");

        card.add(icon, cardTitle, cardValue, cardDescription);
        return card;
    }
}