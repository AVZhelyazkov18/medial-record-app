package bg.nbu.medialrecordapp.ui.view.doctor;

import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorHomeResponseDTO;
import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorAppointmentHomeDTO;
import bg.nbu.medialrecordapp.data.dto.Patient.PatientSearchResponseDTO;
import bg.nbu.medialrecordapp.service.doctor.DoctorService;
import bg.nbu.medialrecordapp.service.doctor.DoctorServiceImpl;
import bg.nbu.medialrecordapp.service.patient.PatientService;
import bg.nbu.medialrecordapp.service.patient.PatientServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Set;

@Route("doctor-home")
@RolesAllowed("DOCTOR")
public class DoctorHomeView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";
    private static final String BORDER_COLOR = "#BBF7D0";
    private static final String ICON_COLOR = "#16A34A";

    private final DoctorService doctorService;
    private final PatientService patientService;

    public DoctorHomeView(
            DoctorService doctorService,
            AuthenticationContext authenticationContext,
            PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;

        DoctorHomeResponseDTO doctorResponseDTO = this.doctorService.getCurrentDoctorHomeInfo();

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

        Div hero = createHeroSection(doctorResponseDTO, authenticationContext);

        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setWidthFull();
        mainContent.setSpacing(true);
        mainContent.setPadding(false);
        mainContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        mainContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        mainContent.add(
                createAppointmentsSection(doctorResponseDTO.getNextAppointments()),
                createPatientSearchSection()
        );

        Div statistics = createStatisticsSection(doctorResponseDTO);

        page.add(hero, mainContent, statistics);
        add(page);
    }

    private Div createHeroSection(
            DoctorHomeResponseDTO doctorHome,
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

        H1 title = new H1("Doctor Dashboard");
        title.getStyle()
                .set("margin", "0")
                .set("font-size", "36px")
                .set("color", TITLE_COLOR);

        Span doctorName = new Span(
                "Dr. " + doctorHome.getFirstName() + " " + doctorHome.getLastName()
        );
        doctorName.getStyle()
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

        HorizontalLayout userActions = new HorizontalLayout(doctorName, logoutButton);
        userActions.setAlignItems(FlexComponent.Alignment.CENTER);

        header.add(title, userActions);

        Paragraph subtitle = new Paragraph("Welcome to your medical work dashboard.");
        subtitle.getStyle()
                .set("max-width", "720px")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "16px")
                .set("line-height", "1.6")
                .set("margin", "16px 0 0 0");

        hero.add(header, subtitle);
        return hero;
    }

    private Div createAppointmentsSection(List<DoctorAppointmentHomeDTO> appointments) {
        Div card = createBaseCard("Next Appointments", "Current next three appointments");

        if (appointments.isEmpty()) {
            Paragraph empty = new Paragraph("No upcoming appointments.");
            empty.getStyle()
                    .set("color", SUBTITLE_COLOR)
                    .set("margin", "16px 0 0 0");

            card.add(empty);
            return card;
        }

        for (DoctorAppointmentHomeDTO appointment : appointments) {
            Div appointmentBox = new Div();

            appointmentBox.getStyle()
                    .set("background", BACKGROUND_COLOR)
                    .set("border", "1px solid " + BORDER_COLOR)
                    .set("border-radius", "14px")
                    .set("padding", "14px")
                    .set("margin-top", "12px")
                    .set("cursor", "pointer");

            Span id = new Span("Appointment #" + appointment.getId());
            id.getStyle()
                    .set("display", "block")
                    .set("font-weight", "700")
                    .set("color", TITLE_COLOR);

            Span patient = new Span(appointment.getPatientFullName());
            patient.getStyle()
                    .set("display", "block")
                    .set("color", SUBTITLE_COLOR)
                    .set("font-size", "14px")
                    .set("margin-top", "4px");

            Span time = new Span(appointment.getAppointmentDate().toString());
            time.getStyle()
                    .set("display", "block")
                    .set("color", SUBTITLE_COLOR)
                    .set("font-size", "13px")
                    .set("margin-top", "4px");

            appointmentBox.add(id, patient, time);

            appointmentBox.addClickListener(event ->
                    getUI().ifPresent(ui ->
                            ui.navigate("fill-appointment/" + appointment.getId())
                    )
            );

            card.add(appointmentBox);
        }

        return card;
    }

    private Div createPatientSearchSection() {
        Div card = createBaseCard("Search Patient", "Find patient information");

        ComboBox<PatientSearchResponseDTO> patientComboBox = new ComboBox<>("Patient");
        patientComboBox.setWidthFull();
        patientComboBox.setItemLabelGenerator(patient ->
                patient.getFirstName() + " " + patient.getLastName()
        );

        Set<PatientSearchResponseDTO> patients = patientService.getAllPatients();

        patientComboBox.setItems(patients);

        Button searchButton = new Button("Search", new Icon(VaadinIcon.SEARCH));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.setWidthFull();

        searchButton.getStyle()
                .set("margin-top", "12px")
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px");

        searchButton.addClickListener(event -> {
            PatientSearchResponseDTO selectedPatient = patientComboBox.getValue();

            if (selectedPatient == null) {
                Notification.show("Please select a patient first.");
                return;
            }

            openPatientDialog(selectedPatient);
        });

        card.add(patientComboBox, searchButton);
        return card;
    }

    private Div createStatisticsSection(DoctorHomeResponseDTO doctorHome) {
        Div wrapper = new Div();

        wrapper.getStyle()
                .set("width", "100%")
                .set("max-width", "950px")
                .set("margin-top", "8px");

        HorizontalLayout stats = new HorizontalLayout();
        stats.setWidthFull();
        stats.setSpacing(true);
        stats.setAlignItems(FlexComponent.Alignment.STRETCH);
        stats.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        stats.add(
                createStatCard(
                        VaadinIcon.STETHOSCOPE,
                        "Most found diagnosis",
                        doctorHome.getMostFoundDiagnosis()
                ),
                createStatCard(
                        VaadinIcon.USERS,
                        "GP patients",
                        String.valueOf(doctorHome.getGpPatientsCount())
                ),
                createStatCard(
                        VaadinIcon.FILE_TEXT,
                        "Medical records",
                        String.valueOf(doctorHome.getMedicalRecordsCount())
                ),
                createStatCard(
                        VaadinIcon.MONEY,
                        "Total earnings",
                        doctorHome.getTotalEarnings() + " EURO"
                )
        );

        wrapper.add(stats);
        return wrapper;
    }

    private Div createStatCard(VaadinIcon iconType, String title, String value) {
        Div card = new Div();

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("padding", "20px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("width", "210px");

        Icon icon = new Icon(iconType);
        icon.getStyle()
                .set("width", "28px")
                .set("height", "28px")
                .set("color", ICON_COLOR)
                .set("background", BACKGROUND_COLOR)
                .set("border-radius", "12px")
                .set("padding", "10px");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle()
                .set("font-size", "15px")
                .set("margin", "14px 0 6px 0")
                .set("color", TITLE_COLOR);

        Span cardValue = new Span(value == null || value.isBlank() ? "N/A" : value);
        cardValue.getStyle()
                .set("display", "block")
                .set("font-weight", "700")
                .set("color", TITLE_COLOR)
                .set("font-size", "18px");

        card.add(icon, cardTitle, cardValue);
        return card;
    }

    private Div createBaseCard(String title, String subtitle) {
        Div card = new Div();

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("padding", "24px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("width", "430px")
                .set("max-width", "430px");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle()
                .set("font-size", "22px")
                .set("margin", "0")
                .set("color", TITLE_COLOR);

        Paragraph cardSubtitle = new Paragraph(subtitle);
        cardSubtitle.getStyle()
                .set("margin", "6px 0 18px 0")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "14px");

        card.add(cardTitle, cardSubtitle);
        return card;
    }

    private void openPatientDialog(PatientSearchResponseDTO patient) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Patient Information");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        content.add(
                new Paragraph("Name: " + patient.getFirstName() + " " + patient.getLastName()),
                new Paragraph("Email: " + patient.getEmail()),
                new Paragraph("Health insurance: " + patient.getHealthInsuranceStatus()),
                new Paragraph("General practitioner: " + patient.getGeneralPractitionerName())
        );

        Button closeButton = new Button("Close", event -> dialog.close());
        closeButton.getStyle()
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border-radius", "10px");

        dialog.add(content);
        dialog.getFooter().add(closeButton);
        dialog.open();
    }
}
