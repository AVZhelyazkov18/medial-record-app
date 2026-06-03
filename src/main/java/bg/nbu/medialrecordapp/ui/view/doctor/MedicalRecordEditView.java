package bg.nbu.medialrecordapp.ui.view.doctor;

import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.data.entity.FitNote;
import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import bg.nbu.medialrecordapp.service.doctor.DoctorService;
import bg.nbu.medialrecordapp.service.examinations.FitNoteService;
import bg.nbu.medialrecordapp.service.examinations.MedicalRecordService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;

@Route("fill-appointment/:appointmentId")
@PageTitle("Edit Medical Record")
@RolesAllowed("DOCTOR")
public class MedicalRecordEditView extends VerticalLayout implements BeforeEnterObserver {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String BORDER_COLOR = "#BBF7D0";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";

    private final MedicalRecordService medicalRecordService;
    private final DoctorService doctorService;
    private final FitNoteService fitNoteService;

    public MedicalRecordEditView(MedicalRecordService medicalRecordService,
                                 DoctorService doctorService, FitNoteService fitNoteService) {
        this.medicalRecordService = medicalRecordService;
        this.doctorService = doctorService;
        this.fitNoteService = fitNoteService;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        getStyle().set("background", "linear-gradient(180deg, #F7FFF9 0%, #ECFDF3 100%)");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idParam = event.getRouteParameters().get("appointmentId").orElse(null);
        if (idParam == null) {
            event.forwardTo("404");
            return;
        }
        Long appointmentId = Long.parseLong(idParam);
        MedicalRecord record = medicalRecordService.getMedicalRecordById(appointmentId);
        if (record == null) {
            event.forwardTo("404");
            return;
        }
        if (!record.getDoctor().getId().equals(doctorService.getDoctorFromAuthentication().getId())) {
            event.forwardTo("403");
            return;
        }
        createForm(record);
    }

    private void createForm(MedicalRecord record) {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth("500px");
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        TextArea diagnosis = new TextArea("Diagnosis");
        diagnosis.setWidthFull();
        diagnosis.setValue(record.getDiagnosis() != null ? record.getDiagnosis() : "");
        formLayout.add(diagnosis);

        TextArea treatment = new TextArea("Treatment");
        treatment.setWidthFull();
        treatment.setValue(record.getTreatment() != null ? record.getTreatment() : "");
        formLayout.add(treatment);

        NumberField appointmentPrice = new NumberField("Appointment Price");
        appointmentPrice.setWidthFull();
        appointmentPrice.setValue(record.getAppointmentPrice() != null ? record.getAppointmentPrice().doubleValue() : 0.0);
        formLayout.add(appointmentPrice);

        HorizontalLayout fitLayout = new HorizontalLayout();
        fitLayout.setWidthFull();
        fitLayout.setSpacing(true);
        fitLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        DatePicker fitStartDate = new DatePicker("Fit Note Start Date");
        NumberField fitDays = new NumberField("Days Valid");
        fitStartDate.setVisible(false);
        fitDays.setVisible(false);
        fitDays.setWidth("80px");

        Button addFitNoteButton = new Button("+ Add Fit Note");
        addFitNoteButton.setWidthFull();
        addFitNoteButton.getStyle()
                .set("background-color", "#16A34A")
                .set("color", "white")
                .set("border-radius", "8px");

        addFitNoteButton.addClickListener(_ -> {
            FitNote note = fitNoteService.create(record, LocalDate.now(), 0);
            record.setFitNote(note);

            fitStartDate.setValue(LocalDate.now());
            fitStartDate.setVisible(true);
            fitDays.setValue(0.0);
            fitDays.setVisible(true);

            addFitNoteButton.setVisible(false);
        });

        if (record.getFitNote() != null) {
            fitStartDate.setVisible(true);
            fitStartDate.setValue(record.getFitNote().getIssuedOn());
            fitDays.setVisible(true);
            fitDays.setValue((double) record.getFitNote().getLeaveDays());
            addFitNoteButton.setVisible(false);
        }

        fitLayout.add(fitStartDate, fitDays, addFitNoteButton);
        formLayout.add(fitLayout);

        Button goBack = new Button("Go Back", _ -> getUI().ifPresent(ui -> ui.navigate("doctor-home")));
        Button cancelAppointment = new Button("Cancel Appointment", _ -> {
            record.setStatus(MedicalRecordStatus.CANCELLED);
            medicalRecordService.save(record);
            Notification.show("Appointment cancelled");
            getUI().ifPresent(ui -> ui.navigate("doctor-home"));
        });
        cancelAppointment.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button submit = new Button("Submit", _ -> {
            record.setDiagnosis(diagnosis.getValue());
            record.setTreatment(treatment.getValue());
            record.setAppointmentPrice(java.math.BigDecimal.valueOf(appointmentPrice.getValue()));

            FitNote note = record.getFitNote();
            if (note == null) {
                note = fitNoteService.create(record, fitStartDate.getValue(), fitDays.getValue().intValue());
                note.setMedicalRecord(record);
            }
            note.setIssuedOn(fitStartDate.getValue());
            note.setLeaveDays(fitDays.getValue().longValue());
            record.setFitNote(note);

            record.setStatus(MedicalRecordStatus.COMPLETED);
            medicalRecordService.save(record);
            Notification.show("Medical record updated");
            getUI().ifPresent(ui -> ui.navigate("doctor-home"));
        });
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttons = new HorizontalLayout(goBack, cancelAppointment, submit);
        buttons.setSpacing(true);
        formLayout.add(buttons);

        add(formLayout);
    }
}