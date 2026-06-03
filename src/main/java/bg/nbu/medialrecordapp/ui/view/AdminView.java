package bg.nbu.medialrecordapp.ui.view;

import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorInsertRequestDTO;
import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordInsertRequestDTO;
import bg.nbu.medialrecordapp.data.dto.WebAccount.WebAccountInsertRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.FitNote;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import bg.nbu.medialrecordapp.enums.auth.Role;
import bg.nbu.medialrecordapp.service.auth.WebAccountService;
import bg.nbu.medialrecordapp.service.doctor.DoctorService;
import bg.nbu.medialrecordapp.service.examinations.FitNoteService;
import bg.nbu.medialrecordapp.service.examinations.MedicalRecordService;
import bg.nbu.medialrecordapp.service.patient.PatientService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Route("admin")
@PageTitle("Admin Dashboard")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {

    private static final String TITLE_COLOR = "#163F17";
    private static final String SUBTITLE_COLOR = "#5F8072";
    private static final String BACKGROUND_COLOR = "#E6FFEB";
    private static final String GLOW_COLOR = "rgba(31, 125, 75, 0.10)";
    private static final String BORDER_COLOR = "#BBF7D0";

    private final WebAccountService accountService;
    private final DoctorService doctorService;
    private final MedicalRecordService medicalRecordService;
    private final FitNoteService fitNoteService;
    private final AuthenticationContext authenticationContext;
    private final PatientService patientService;

    private Map<String, Long> top3Doctors;

    public AdminView(WebAccountService accountService,
                     DoctorService doctorService,
                     MedicalRecordService medicalRecordService,
                     FitNoteService fitNoteService,
                     AuthenticationContext authenticationContext, PatientService patientService) {
        this.accountService = accountService;
        this.doctorService = doctorService;
        this.medicalRecordService = medicalRecordService;
        this.fitNoteService = fitNoteService;
        this.authenticationContext = authenticationContext;

        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", BACKGROUND_COLOR);

        add(buildTabs());
        add(buildStatistics());
        this.patientService = patientService;
    }

    private VerticalLayout buildTabs() {
        Tab accountsTab = new Tab("Accounts");
        Tab doctorsTab = new Tab("Doctors");
        Tab recordsTab = new Tab("Medical Records");
        Tab fitNotesTab = new Tab("FitNotes");

        accountsTab.getStyle().set("color", SUBTITLE_COLOR);
        doctorsTab.getStyle().set("color", SUBTITLE_COLOR);
        recordsTab.getStyle().set("color", SUBTITLE_COLOR);
        fitNotesTab.getStyle().set("color", SUBTITLE_COLOR);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(accountsTab, createAccountsGrid());
        tabsToPages.put(doctorsTab, createDoctorsGrid());
        tabsToPages.put(recordsTab, createMedicalRecordsGrid());
        tabsToPages.put(fitNotesTab, createFitNotesGrid());

        Button logoutButton = new Button(
                "Logout",
                new Icon(VaadinIcon.SIGN_OUT),
                _ -> authenticationContext.logout()
        );
        logoutButton.getStyle()
                .set("background-color", TITLE_COLOR)
                .set("color", "white")
                .set("border", "1px solid " + TITLE_COLOR)
                .set("border-radius", "10px")
                .set("height", "34px")
                .set("padding", "0 14px")
                .set("font-size", "13px");

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Tabs tabs = new Tabs(accountsTab, doctorsTab, recordsTab, fitNotesTab);

        headerLayout.add(tabs, logoutButton);

        VerticalLayout pagesLayout = new VerticalLayout();
        pagesLayout.setSizeFull();
        pagesLayout.setPadding(false);
        pagesLayout.setSpacing(false);

        tabsToPages.values().forEach(page -> {
            page.setVisible(false);
            if (page instanceof HasSize hasSize) {
                hasSize.setSizeFull();
            }
            pagesLayout.add(page);
        });

        Component firstPage = tabsToPages.get(accountsTab);
        if (firstPage != null)
            firstPage.setVisible(true);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(event.getSelectedTab());
            if (selectedPage != null)
                selectedPage.setVisible(true);
        });

        VerticalLayout layout = new VerticalLayout(headerLayout, pagesLayout);
        layout.setSizeFull();
        return layout;
    }

    private VerticalLayout createAccountsGrid() {
        Grid<WebAccount> grid = new Grid<>(WebAccount.class, false);

        grid.addColumn(WebAccount::getId)
                .setHeader("ID")
                .setWidth("80px")
                .setFlexGrow(0);

        Grid.Column<WebAccount> emailColumn = grid.addColumn(WebAccount::getEmail)
                .setHeader("Email")
                .setWidth("250px");

        Grid.Column<WebAccount> roleColumn = grid.addColumn(account -> account.getRole().name())
                .setHeader("Role")
                .setWidth("150px");

        Binder<WebAccount> binder = new Binder<>(WebAccount.class);
        Editor<WebAccount> editor = grid.getEditor();
        editor.setBuffered(false);
        editor.setBinder(binder);

        TextField emailEditor = new TextField();
        emailEditor.setWidthFull();
        binder.forField(emailEditor)
                .bind(WebAccount::getEmail, WebAccount::setEmail);
        emailColumn.setEditorComponent(emailEditor);

        ComboBox<Role> roleEditor = new ComboBox<>();
        roleEditor.setItems(Role.values());
        roleEditor.setItemLabelGenerator(Enum::name);
        binder.forField(roleEditor)
                .bind(WebAccount::getRole, WebAccount::setRole);
        roleColumn.setEditorComponent(roleEditor);

        binder.addValueChangeListener(_ -> {
            WebAccount item = editor.getItem();
            if (item != null) {
                accountService.save(item);
                grid.getDataProvider().refreshItem(item);
            }
        });

        grid.addItemDoubleClickListener(e -> {
            editor.editItem(e.getItem());
            emailEditor.focus();
        });

        grid.addComponentColumn(account -> {
                    Button delete = new Button("Delete", _ -> {
                        accountService.delete(account);
                        grid.setItems(accountService.findAll());
                    });
                    delete.getStyle().set("color", "darkred");
                    return delete;
                }).setHeader("Actions")
                .setFlexGrow(0)
                .setWidth("120px");

        grid.setItems(accountService.findAll());
        grid.setSizeFull();

        Button add = createAddButton("Add Account", () -> {
            WebAccountInsertRequestDTO dto = new WebAccountInsertRequestDTO();
            dto.setEmail("");
            dto.setRole(Role.PATIENT);

            WebAccount saved = accountService.save(dto);

            grid.setItems(accountService.findAll());
            grid.select(saved);
            grid.getEditor().editItem(saved);
        });

        return wrapGridWithAdd(grid, add);
    }

    private VerticalLayout createDoctorsGrid() {
        Grid<Doctor> grid = new Grid<>(Doctor.class, false);

        grid.addColumn(Doctor::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        Grid.Column<Doctor> firstNameColumn = grid.addColumn(Doctor::getFirstName).setHeader("First Name").setWidth("150px");
        Grid.Column<Doctor> lastNameColumn = grid.addColumn(Doctor::getLastName).setHeader("Last Name").setWidth("150px");
        Grid.Column<Doctor> specialtyColumn = grid.addColumn(Doctor::getSpecialty).setHeader("Specialty").setWidth("150px");
        Grid.Column<Doctor> qualifiedColumn = grid.addColumn(d -> d.isQualifiedForGeneralPractitioner() ? "Yes" : "No")
                .setHeader("Qualified GP").setWidth("120px");
        Grid.Column<Doctor> emailColumn = grid.addColumn(d -> {
            if(d.getWebAccount() == null)
                return "";
            else
                return d.getWebAccount().getEmail();
        }
        ).setHeader("Email").setWidth("250px");

        Binder<Doctor> binder = new Binder<>(Doctor.class);
        Editor<Doctor> editor = grid.getEditor();
        editor.setBuffered(false);
        editor.setBinder(binder);

        TextField firstNameEditor = new TextField();
        firstNameEditor.setWidthFull();
        binder.forField(firstNameEditor).bind(Doctor::getFirstName, Doctor::setFirstName);
        firstNameColumn.setEditorComponent(firstNameEditor);

        TextField lastNameEditor = new TextField();
        lastNameEditor.setWidthFull();
        binder.forField(lastNameEditor).bind(Doctor::getLastName, Doctor::setLastName);
        lastNameColumn.setEditorComponent(lastNameEditor);

        TextField specialtyEditor = new TextField();
        specialtyEditor.setWidthFull();
        binder.forField(specialtyEditor).bind(Doctor::getSpecialty, Doctor::setSpecialty);
        specialtyColumn.setEditorComponent(specialtyEditor);

        Checkbox qualifiedEditor = new Checkbox();
        binder.forField(qualifiedEditor).bind(Doctor::isQualifiedForGeneralPractitioner, Doctor::setQualifiedForGeneralPractitioner);
        qualifiedColumn.setEditorComponent(qualifiedEditor);

        TextField emailEditor = new TextField();
        emailEditor.setWidthFull();
        binder.forField(emailEditor)
                .bind(
                        d -> d.getWebAccount() != null ? d.getWebAccount().getEmail() : "",
                        (d, value) -> {
                            if (value == null || value.isBlank()) {
                                d.setWebAccount(null);
                            } else {
                                WebAccount account = accountService.findByEmail(value);
                                if (account != null) {
                                    d.setWebAccount(account);
                                    doctorService.save(d);
                                } else {
                                    emailEditor.setValue("");
                                }
                            }

                        }
                );
        emailColumn.setEditorComponent(emailEditor);

        grid.addComponentColumn(d -> {
            Button delete = new Button("Delete", _ -> {
                doctorService.delete(d);
                grid.setItems(doctorService.findAll());
            });
            delete.getStyle().set("color", "darkred");
            return delete;
        }).setHeader("Actions").setFlexGrow(0).setWidth("120px");

        binder.addValueChangeListener(_ -> {
            Doctor item = editor.getItem();
            if (item != null) {
                doctorService.save(item);
                grid.getDataProvider().refreshItem(item);
            }
        });

        grid.addItemDoubleClickListener(e -> {
            editor.editItem(e.getItem());
            firstNameEditor.focus();
        });

        grid.setItems(doctorService.findAll());
        grid.setSizeFull();

        Button add = createAddButton("Add Doctor", () -> {
            DoctorInsertRequestDTO dto = new DoctorInsertRequestDTO();
            dto.setFirstName(firstNameEditor.getValue());
            dto.setLastName(lastNameEditor.getValue());
            dto.setSpecialty(specialtyEditor.getValue());
            dto.setQualifiedForGeneralPractitioner(qualifiedEditor.getValue());

            Doctor saved = doctorService.save(dto);
            grid.setItems(doctorService.findAll());
            grid.select(saved);
            grid.getEditor().editItem(saved);
        });

        return wrapGridWithAdd(grid, add);
    }

    private VerticalLayout createMedicalRecordsGrid() {
        Grid<MedicalRecord> grid = new Grid<>(MedicalRecord.class, false);

        grid.addColumn(MedicalRecord::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        Grid.Column<MedicalRecord> patientColumn = grid.addColumn(r -> {
            if (r.getPatient() != null && r.getPatient().getWebAccount() != null)
                return r.getPatient().getWebAccount().getEmail();
            return "";
        }).setHeader("Patient").setWidth("200px");
        Grid.Column<MedicalRecord> doctorColumn = grid.addColumn(r -> {
            if (r.getDoctor() != null && r.getDoctor().getWebAccount() != null) {
                return r.getDoctor().getWebAccount().getEmail();
            }
            return "";
        }).setHeader("Doctor").setWidth("200px");
        Grid.Column<MedicalRecord> statusColumn = grid.addColumn(MedicalRecord::getStatus)
                .setHeader("Status").setWidth("150px");
        Grid.Column<MedicalRecord> dateColumn = grid.addColumn(MedicalRecord::getDate)
                .setHeader("Checkout Date").setWidth("150px");
        Grid.Column<MedicalRecord> diagnosisColumn = grid.addColumn(MedicalRecord::getDiagnosis)
                .setHeader("Diagnosis").setWidth("200px");
        Grid.Column<MedicalRecord> treatmentColumn = grid.addColumn(MedicalRecord::getTreatment)
                .setHeader("Treatment").setWidth("200px");
        Grid.Column<MedicalRecord> priceColumn = grid.addColumn(MedicalRecord::getAppointmentPrice)
                .setHeader("Price").setWidth("120px");
        Grid.Column<MedicalRecord> fitNoteColumn = grid.addColumn(
                r -> r.getFitNote() != null ? r.getFitNote().getId() : null
        ).setHeader("FitNote ID").setWidth("120px");

        Binder<MedicalRecord> binder = new Binder<>(MedicalRecord.class);
        Editor<MedicalRecord> editor = grid.getEditor();
        editor.setBuffered(false);
        editor.setBinder(binder);

        DatePicker dateEditor = new DatePicker();
        binder.forField(dateEditor).bind(MedicalRecord::getDate, MedicalRecord::setDate);
        dateColumn.setEditorComponent(dateEditor);

        TextField diagnosisEditor = new TextField();
        diagnosisEditor.setWidthFull();
        binder.forField(diagnosisEditor).bind(MedicalRecord::getDiagnosis, MedicalRecord::setDiagnosis);
        diagnosisColumn.setEditorComponent(diagnosisEditor);

        TextField treatmentEditor = new TextField();
        treatmentEditor.setWidthFull();
        binder.forField(treatmentEditor).bind(MedicalRecord::getTreatment, MedicalRecord::setTreatment);
        treatmentColumn.setEditorComponent(treatmentEditor);

        NumberField priceEditor = new NumberField();
        binder.forField(priceEditor)
                .bind(r -> r.getAppointmentPrice() != null ? r.getAppointmentPrice().doubleValue() : 0,
                        (r, val) -> r.setAppointmentPrice(BigDecimal.valueOf(val)));
        priceColumn.setEditorComponent(priceEditor);

        TextField patientEmailEditor = new TextField();
        patientEmailEditor.setWidthFull();

        binder.forField(patientEmailEditor)
                .bind(
                        r -> r.getPatient() != null ? r.getPatient().getWebAccount().getEmail() : "",
                        (r, email) -> {
                            if (email == null || email.isBlank()) {
                                r.setPatient(null);
                            } else {
                                Patient patient = patientService.findByEmail(email);
                                if (patient != null)
                                    r.setPatient(patient);
                            }
                        }
                );
        patientColumn.setEditorComponent(patientEmailEditor);

        IntegerField fitNoteEditor = new IntegerField();
        fitNoteEditor.setWidthFull();

        binder.forField(fitNoteEditor)
                .bind(
                        r -> Math.toIntExact((r.getFitNote() != null && r.getFitNote().getId() != null
                                ? r.getFitNote().getId()
                                : -1)),
                        (r, val) -> {
                            if (val == null || val.equals(-1)) {
                                r.setFitNote(null);
                            } else {
                                try {
                                    FitNote note = fitNoteService.findById(val.longValue());
                                    if (note != null) {
                                        r.setFitNote(note);
                                    } else {
                                        fitNoteEditor.setValue(-1);
                                    }
                                } catch (NumberFormatException ex) {
                                    fitNoteEditor.setValue(-1);
                                }
                            }
                            medicalRecordService.save(r);
                        }
                );
        fitNoteColumn.setEditorComponent(fitNoteEditor);

        ComboBox<MedicalRecordStatus> statusEditor = new ComboBox<>();
        statusEditor.setItems(MedicalRecordStatus.values());
        binder.forField(statusEditor).bind(MedicalRecord::getStatus, MedicalRecord::setStatus);
        statusColumn.setEditorComponent(statusEditor);

        TextField doctorEmailEditor = new TextField();
        doctorEmailEditor.setWidthFull();

        binder.forField(doctorEmailEditor)
                .bind(
                        record -> record.getDoctor() != null ? record.getDoctor().getWebAccount().getEmail() : "",
                        (record, email) -> {
                            if (email == null || email.isBlank()) {
                                record.setDoctor(null);
                            } else {
                                Doctor doctor = doctorService.findByEmail(email);
                                if (doctor != null)
                                    record.setDoctor(doctor);
                            }
                            medicalRecordService.save(record);
                        }
                );

        doctorColumn.setEditorComponent(doctorEmailEditor);

        binder.addValueChangeListener(_ -> {
            MedicalRecord item = editor.getItem();
            if (item != null) {
                medicalRecordService.save(item);
                grid.getDataProvider().refreshItem(item);
            }
        });

        grid.addItemDoubleClickListener(e -> {
            editor.editItem(e.getItem());
            dateEditor.focus();
        });

        grid.addComponentColumn(record -> {
            Button delete = new Button("Delete", _ -> {
                medicalRecordService.delete(record);
                grid.setItems(medicalRecordService.findAll());
            });
            delete.getStyle().set("color", "darkred");
            return delete;
        }).setHeader("Actions").setFlexGrow(0).setWidth("120px");

        grid.setItems(medicalRecordService.findAll());
        grid.setSizeFull();

        Button add = createAddButton("Add Record", () -> {
            MedicalRecordInsertRequestDTO dto = new MedicalRecordInsertRequestDTO();

            dto.setCheckoutDate(LocalDate.now());
            dto.setDiagnosis("");
            dto.setTreatment("");
            dto.setAppointmentPrice(BigDecimal.ZERO);

            MedicalRecord saved = medicalRecordService.save(dto);

            grid.setItems(medicalRecordService.findAll());
            grid.select(saved);
            grid.getEditor().editItem(saved);
        });

        return wrapGridWithAdd(grid, add);
    }

    private VerticalLayout createFitNotesGrid() {
        Grid<FitNote> grid = new Grid<>(FitNote.class, false);

        grid.addColumn(FitNote::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        Grid.Column<FitNote> medicalRecordColumn = grid.addColumn(f -> f.getMedicalRecord() != null ? f.getMedicalRecord().getId() : null)
                .setHeader("MedicalRecord ID").setWidth("150px");
        Grid.Column<FitNote> issuedOnColumn = grid.addColumn(FitNote::getIssuedOn)
                .setHeader("Issued On").setWidth("150px");
        Grid.Column<FitNote> leaveDaysColumn = grid.addColumn(FitNote::getLeaveDays)
                .setHeader("Leave Days").setWidth("120px");

        Binder<FitNote> binder = new Binder<>(FitNote.class);
        Editor<FitNote> editor = grid.getEditor();
        editor.setBuffered(false);
        editor.setBinder(binder);

        DatePicker issuedOnEditor = new DatePicker();
        binder.forField(issuedOnEditor).bind(FitNote::getIssuedOn, FitNote::setIssuedOn);
        issuedOnColumn.setEditorComponent(issuedOnEditor);

        NumberField leaveDaysEditor = new NumberField();
        binder.forField(leaveDaysEditor)
                .bind(f -> f.getLeaveDays() != null ? f.getLeaveDays().doubleValue() : 0,
                        (f, val) -> f.setLeaveDays(val != null ? val.longValue() : 0L));
        leaveDaysColumn.setEditorComponent(leaveDaysEditor);

        binder.addValueChangeListener(_ -> {
            FitNote item = editor.getItem();
            if (item != null) {
                fitNoteService.save(item);
                grid.getDataProvider().refreshItem(item);
            }
        });

        grid.addItemDoubleClickListener(e -> {
            editor.editItem(e.getItem());
            issuedOnEditor.focus();
        });

        grid.addComponentColumn(f -> {
            Button delete = new Button("Delete", _ -> {
                fitNoteService.delete(f);
                grid.setItems(fitNoteService.findAll());
            });
            delete.getStyle().set("color", "darkred");
            return delete;
        }).setHeader("Actions").setFlexGrow(0).setWidth("120px");

        grid.setItems(fitNoteService.findAll());
        grid.setSizeFull();

        return wrapGridWithAdd(grid, null);
    }

    private VerticalLayout wrapGridWithAdd(Grid<?> grid, Button add) {

        VerticalLayout layout;

        if (add != null)
            layout = new VerticalLayout(grid, add);
        else
            layout = new VerticalLayout(grid);

        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.getStyle().set("border", "1px solid " + BORDER_COLOR);
        layout.getStyle().set("box-shadow", "0px 6px 18px " + GLOW_COLOR);
        layout.getStyle().set("border-radius", "20px");
        return layout;
    }

    private Button createAddButton(String label, Runnable onClick) {
        Button add = new Button(label, _ -> onClick.run());
        add.getStyle().set("background-color", TITLE_COLOR);
        add.getStyle().set("color", "white");
        add.getStyle().set("border-radius", "10px");
        add.getStyle().set("box-shadow", "0 4px 12px " + GLOW_COLOR);
        return add;
    }

    private HorizontalLayout buildStatistics() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.setAlignItems(Alignment.STRETCH);
        statsLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        String monthMostFitNotes = fitNoteService.getMonthWithMostFitNotes();
        List<String> month = Collections.singletonList(monthMostFitNotes);
        top3Doctors = doctorService.getTop3DoctorsByFitNotes();

        statsLayout.add(
                createStatCard(VaadinIcon.CALENDAR, "Month with most FitNotes", month)
        );


        List<String> topDoctorStrings = new ArrayList<>();
        for (Map.Entry<String, Long> entry : top3Doctors.entrySet()) {
            String formatted = String.format("Dr. %s - %d FitNotes", entry.getKey(), entry.getValue());
            topDoctorStrings.add(formatted);
        }

        statsLayout.add(createStatCard(VaadinIcon.USER_STAR, "Top 3 Doctors", topDoctorStrings));
        return statsLayout;
    }

    private Div createStatCard(VaadinIcon iconType, String title, List<String> values) {
        Div card = new Div();

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("padding", "20px")
                .set("box-shadow", "0 8px 24px " + GLOW_COLOR)
                .set("border", "1px solid " + BORDER_COLOR)
                .set("width", "220px");

        Icon icon = new Icon(iconType);
        icon.getStyle()
                .set("width", "28px")
                .set("height", "28px")
                .set("color", TITLE_COLOR)
                .set("background", BACKGROUND_COLOR)
                .set("border-radius", "12px")
                .set("padding", "10px");

        card.add(icon);

        Div textDiv = new Div();
        textDiv.getStyle().set("margin-top", "10px");
        textDiv.add(new Div(new Text(title)));
        for (String value : values) {
            textDiv.add(new Div(new Text(value)));
        }
        card.add(textDiv);

        return card;
    }
}