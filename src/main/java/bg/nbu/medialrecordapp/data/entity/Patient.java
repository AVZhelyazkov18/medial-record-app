package bg.nbu.medialrecordapp.data.entity;

import bg.nbu.medialrecordapp.data.dto.RegisterPatientRequestDTO;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.enums.HealthInsuranceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "patient")
public class Patient extends BaseEntity {
    protected Patient() {}

    public Patient(RegisterPatientRequestDTO registerPatientRequestDTO, WebAccount webAccount) {
        this.firstName = registerPatientRequestDTO.getFirstName();
        this.lastName = registerPatientRequestDTO.getLastName();
        this.generalPractitioner = null;
        this.healthInsuranceStatus = HealthInsuranceStatus.EXPIRED;
        this.medicalRecords = null;

        this.webAccount = webAccount;
    }

    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    private String lastName;

    @JoinColumn(name = "general_practitioner_id")
    @ManyToOne
    private Doctor generalPractitioner;

    @Column(name = "health_insurance_status")
    private HealthInsuranceStatus healthInsuranceStatus;

    @OneToMany(mappedBy = "patient")
    private Set<MedicalRecord> medicalRecords;

    @OneToOne
    @JoinColumn(name = "web_account_id", nullable = false, unique = true)
    private WebAccount webAccount;
}
