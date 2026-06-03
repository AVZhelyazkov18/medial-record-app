package bg.nbu.medialrecordapp.data.dto.Doctor;

import bg.nbu.medialrecordapp.data.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DoctorAppointmentHomeDTO {
    private Long id;
    private String patientFullName;
    private LocalDate appointmentDate;
}
