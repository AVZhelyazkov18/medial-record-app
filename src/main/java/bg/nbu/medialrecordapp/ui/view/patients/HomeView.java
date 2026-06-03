package bg.nbu.medialrecordapp.ui.view.patients;

import bg.nbu.medialrecordapp.data.dto.Patient.PatientHomeResponseDTO;
import bg.nbu.medialrecordapp.service.patient.PatientService;
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
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;

@Route("my-health")
@RolesAllowed("PATIENT")
public class HomeView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";
    private static final String BORDER_COLOR = "#BBF7D0";
    private static final String ICON_COLOR = "#16A34A";

    public HomeView(
            PatientService patientService,
            AuthenticationContext authenticationContext
    ) {
        PatientHomeResponseDTO patient = patientService.getCurrentPatientHomeInfo();

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

        Div hero = createHeroSection(patient, authenticationContext);

        HorizontalLayout cards = new HorizontalLayout();
        cards.setSpacing(true);
        cards.setPadding(false);
        cards.setAlignItems(FlexComponent.Alignment.STRETCH);
        cards.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cards.getStyle()
                .set("width", "fit-content")
                .set("max-width", "100%")
                .set("margin", "0 auto");

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

        Button createAppointmentButton = new Button(
                "Create Appointment",
                new Icon(VaadinIcon.CALENDAR),
                _ -> getUI().ifPresent(ui -> ui.navigate("new-appointment"))
        );

        createAppointmentButton.getStyle()
                .set("margin-top", "8px")
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px");

        Button examinationsButton = new Button(
                "View Examinations",
                new Icon(VaadinIcon.ARROW_RIGHT),
                _ -> getUI().ifPresent(ui -> ui.navigate("examinations"))
        );

        examinationsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        examinationsButton.getStyle()
                .set("margin-top", "8px")
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px");

        HorizontalLayout actionButtons = new HorizontalLayout(
                createAppointmentButton,
                examinationsButton
        );
        actionButtons.setSpacing(true);
        actionButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        actionButtons.setAlignItems(FlexComponent.Alignment.CENTER);

        page.add(hero, cards, actionButtons);
        add(page);
    }

    private Div createHeroSection(
            PatientHomeResponseDTO patient,
            AuthenticationContext authenticationContext
    ) {
        Div hero = new Div();

        hero.getStyle()
                .set("width", "80%")
                .set("max-width", "950px")
                .set("background", "white")
                .set("border-radius", "24px")
                .set("padding", "32px")
                .set("box-shadow", "0 8px 24px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR);

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setPadding(false);
        header.setSpacing(true);

        H1 title = new H1("My Health Information");
        title.getStyle()
                .set("margin", "0")
                .set("font-size", "36px")
                .set("color", TITLE_COLOR);

        Span patientName = new Span(
                patient.getFirstName() + " " + patient.getLastName()
        );
        patientName.getStyle()
                .set("font-weight", "600")
                .set("font-size", "15px")
                .set("color", TITLE_COLOR);

        Button logoutButton = new Button(
                "Logout",
                new Icon(VaadinIcon.SIGN_OUT),
                event -> authenticationContext.logout()
        );
        logoutButton.getStyle()
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px")
                .set("height", "34px")
                .set("padding", "0 14px")
                .set("font-size", "13px");

        HorizontalLayout userActions = new HorizontalLayout(patientName, logoutButton);
        userActions.setAlignItems(FlexComponent.Alignment.CENTER);
        userActions.setSpacing(true);
        userActions.setPadding(false);

        header.add(title, userActions);

        Paragraph subtitle = new Paragraph(
                "Welcome to your personal health dashboard."
        );
        subtitle.getStyle()
                .set("max-width", "720px")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "16px")
                .set("line-height", "1.6")
                .set("margin", "16px 0 0 0");

        hero.add(header, subtitle);
        return hero;
    }

    private Div createInfoCard(
            VaadinIcon iconType,
            String title,
            String value,
            String description
    ) {
        Div card = new Div();

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("padding", "24px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("width", "280px")
                .set("max-width", "280px");

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