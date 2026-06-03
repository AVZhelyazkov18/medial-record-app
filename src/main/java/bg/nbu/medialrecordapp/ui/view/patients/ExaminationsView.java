package bg.nbu.medialrecordapp.ui.view.patients;

import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordPatientResponseDTO;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.service.examinations.MedicalRecordService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("examinations")
@RolesAllowed("PATIENT")
public class ExaminationsView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";
    private static final String BORDER_COLOR = "#BBF7D0";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final MedicalRecordService medicalRecordService;

    public ExaminationsView(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;

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

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(false);
        header.setSpacing(false);
        header.getStyle()
                .set("position", "relative")
                .set("margin-bottom", "24px");

        Button backButton = new Button(
                "Back",
                new Icon(VaadinIcon.ARROW_LEFT),
                event -> getUI().ifPresent(ui -> ui.navigate("my-health"))
        );

        backButton.getStyle()
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px")
                .set("font-size", "13px")
                .set("height", "34px")
                .set("padding", "0 12px")
                .set("margin-left", "24px");

        H1 title = new H1("Examinations");
        title.getStyle()
                .set("color", TITLE_COLOR)
                .set("font-size", "36px")
                .set("margin", "0")
                .set("position", "absolute")
                .set("left", "50%")
                .set("transform", "translateX(-50%)");

        header.add(backButton, title);

        VerticalLayout recordsContainer = new VerticalLayout();
        recordsContainer.setWidthFull();
        recordsContainer.setSpacing(true);
        recordsContainer.setPadding(false);

        List<MedicalRecordPatientResponseDTO> records = this.medicalRecordService.getCurrentPatientMedicalRecords();

        if (records.isEmpty()) {
            Div emptyCard = createEmptyCard();
            recordsContainer.add(emptyCard);
        } else {
            records.forEach(record ->
                    recordsContainer.add(createMedicalRecordDetails(record))
            );
        }

        page.add(header, recordsContainer);
        add(page);
    }

    private Details createMedicalRecordDetails(MedicalRecordPatientResponseDTO record) {
        String doctorFullName = record.getDoctorFirstName() + " " + record.getDoctorLastName();
        LocalDate checkoutDate = record.getCheckoutDate();

        String summary = String.format("%s - Dr. %s", checkoutDate.format(DATE_FORMATTER), doctorFullName);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);
        content.getStyle()
                .set("padding", "16px 0 4px 0");

        content.add(
                createInfoRow("Diagnosis", record.getDiagnosis()),
                createInfoRow("Treatment", record.getTreatment()),
                createInfoRow("Fit Note", getFitNoteText(record))
        );

        Details details = new Details(summary, content);
        details.setWidthFull();

        details.getStyle()
                .set("background", "white")
                .set("border-radius", "18px")
                .set("padding", "18px 22px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("color", TITLE_COLOR);

        return details;
    }

    private Div createInfoRow(String label, String value) {
        Div row = new Div();
        row.getStyle()
                .set("background", BACKGROUND_COLOR)
                .set("border-radius", "12px")
                .set("padding", "12px 16px")
                .set("border", "1px solid " + BORDER_COLOR);

        Span labelSpan = new Span(label + ": ");
        labelSpan.getStyle()
                .set("font-weight", "700")
                .set("color", TITLE_COLOR);

        Span valueSpan = new Span(value == null || value.isBlank() ? "Not provided" : value);
        valueSpan.getStyle()
                .set("color", SUBTITLE_COLOR);

        row.add(labelSpan, valueSpan);
        return row;
    }

    private String getFitNoteText(MedicalRecordPatientResponseDTO record) {
        if (record.getFitNoteIssuedOn() == null) {
            return "No fit note issued.";
        }

        return String.format("Fit note issued days: %d", record.getFitNoteLeaveDays());
    }

    private Div createEmptyCard() {
        Div card = new Div();
        card.setText("No examinations found.");
        card.getStyle()
                .set("width", "100%")
                .set("background", "white")
                .set("border-radius", "18px")
                .set("padding", "24px")
                .set("box-shadow", "0 6px 18px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("color", SUBTITLE_COLOR)
                .set("text-align", "center");

        return card;
    }
}