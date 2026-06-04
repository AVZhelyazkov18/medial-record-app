package bg.nbu.medialrecordapp.ui.view.patients;

import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.service.doctor.DoctorService;
import bg.nbu.medialrecordapp.service.doctor.DoctorServiceImpl;
import bg.nbu.medialrecordapp.service.examinations.MedicalRecordService;
import bg.nbu.medialrecordapp.service.examinations.MedicalRecordServiceImpl;
import bg.nbu.medialrecordapp.service.patient.PatientService;
import bg.nbu.medialrecordapp.service.patient.PatientServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.Set;

@Route("new-appointment")
@RolesAllowed("PATIENT")
public class AppointmentView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";
    private static final String BORDER_COLOR = "#BBF7D0";

    public AppointmentView(DoctorService doctorService,
                           MedicalRecordService medicalRecordService,
                           PatientService patientService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(FlexComponent.Alignment.CENTER);

        getStyle()
                .set("background", "linear-gradient(180deg, #F7FFF9 0%, #ECFDF3 100%)")
                .set("min-height", "100vh");

        VerticalLayout page = new VerticalLayout();
        page.setWidthFull();
        page.setMaxWidth("950px");
        page.setPadding(true);
        page.setSpacing(true);
        page.setAlignItems(FlexComponent.Alignment.CENTER);
        page.getStyle().set("padding", "32px");

        Div formCard = new Div();
        formCard.getStyle()
                .set("width", "80%")
                .set("max-width", "650px")
                .set("background", "white")
                .set("border-radius", "24px")
                .set("padding", "32px")
                .set("box-shadow", "0 8px 24px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR);

        H1 title = new H1("Create Appointment");
        title.getStyle()
                .set("margin", "0 0 8px 0")
                .set("font-size", "36px")
                .set("color", TITLE_COLOR)
                .set("text-align", "center");

        Paragraph subtitle = new Paragraph("Select a doctor and choose your appointment date.");
        subtitle.getStyle()
                .set("margin", "0 0 24px 0")
                .set("color", SUBTITLE_COLOR)
                .set("font-size", "16px")
                .set("text-align", "center");

        ComboBox<Doctor> doctorComboBox = new ComboBox<>("Doctor");
        doctorComboBox.setWidthFull();

        Set<Doctor> doctors = doctorService.getAllDoctors();
        doctorComboBox.setItems(doctors);

        doctorComboBox.setItemLabelGenerator(doctor ->
                doctor.getFirstName() + " " + doctor.getLastName()
        );

        DatePicker appointmentDatePicker = new DatePicker("Appointment date");
        appointmentDatePicker.setWidthFull();
        appointmentDatePicker.setMin(LocalDate.now());

        Button backButton = new Button(
                "Back",
                new Icon(VaadinIcon.ARROW_LEFT),
                event -> getUI().ifPresent(ui -> ui.navigate("my-health"))
        );

        backButton.getStyle()
                .set("background-color", "white")
                .set("color", TITLE_COLOR)
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px")
                .set("height", "36px")
                .set("padding", "0 14px");

        Button submitButton = new Button(
                "Create Appointment",
                new Icon(VaadinIcon.CHECK),
                event -> {
                    Doctor selectedDoctor = doctorComboBox.getValue();
                    LocalDate selectedDate = appointmentDatePicker.getValue();
                    Patient patient = patientService.getPatientFromAuthentication();

                    if (selectedDoctor == null) {
                        Notification notification = Notification.show("Please select a doctor.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        return;
                    }

                    if (selectedDate == null) {
                        Notification notification = Notification.show("Please select an appointment date.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        return;
                    }
                    patientService.getCurrentPatientHomeInfo();
                    medicalRecordService.createMedicalRecord(selectedDoctor, selectedDate, patient);

                    Notification notification = Notification.show("Appointment created successfully.");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    getUI().ifPresent(ui -> ui.navigate("my-health"));
                }
        );

        submitButton.getStyle()
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px")
                .set("height", "36px")
                .set("padding", "0 16px");

        HorizontalLayout buttons = new HorizontalLayout(backButton, submitButton);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        buttons.setAlignItems(FlexComponent.Alignment.CENTER);
        buttons.getStyle().set("margin-top", "24px");

        VerticalLayout formLayout = new VerticalLayout(
                title,
                subtitle,
                doctorComboBox,
                appointmentDatePicker,
                buttons
        );
        formLayout.setPadding(false);
        formLayout.setSpacing(true);
        formLayout.setWidthFull();

        formCard.add(formLayout);
        page.add(formCard);
        add(page);
    }
}