package bg.nbu.medialrecordapp.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "fit_note")
@Getter
@Setter
@NoArgsConstructor
public class FitNote extends BaseEntity {

    @CreatedDate
    private LocalDate issuedOn;

    @Column(name = "leave_days")
    private Long leaveDays;

    @OneToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    public FitNote(MedicalRecord record, LocalDate issuedDate, long leaveDays) {
        this.issuedOn = issuedDate;
        this.leaveDays = leaveDays;
        this.medicalRecord = record;
    }
}
