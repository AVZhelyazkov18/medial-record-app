package bg.nbu.medialrecordapp.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

@Entity
public class Checkout extends BaseEntity {
    @Column(name = "checkout_date")
    private LocalDate date;

    @ManyToOne
    @Column(name = "patient_id")
    private Patient patient;

    @Column(name = "doctor_id")
    @ManyToOne()
    private Doctor doctor;

}
