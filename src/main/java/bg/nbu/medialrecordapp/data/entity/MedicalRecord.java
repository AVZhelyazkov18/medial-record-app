package bg.nbu.medialrecordapp.data.entity;

import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medical_record")
@Getter
@Setter
@NoArgsConstructor
public class MedicalRecord extends BaseEntity {
    public MedicalRecord(Doctor doctor, LocalDate checkoutDate, Patient patient) {
        this.doctor = doctor;
        this.date = checkoutDate;
        this.patient = patient;
        this.status = MedicalRecordStatus.SCHEDULED;
    }

    @Column(name = "checkout_date", nullable = false)
    @CreatedDate
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "treatment")
    private String treatment;

    @OneToOne(mappedBy = "medicalRecord")
    private FitNote fitNote;

    @Column(name = "appointmentPrice")
    private BigDecimal appointmentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MedicalRecordStatus status;
}
