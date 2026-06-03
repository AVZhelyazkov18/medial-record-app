package bg.nbu.medialrecordapp.data.dto.Doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DoctorHomeResponseDTO {
    private String firstName;
    private String lastName;
    private List<DoctorAppointmentHomeDTO> nextAppointments;
    private String mostFoundDiagnosis;
    private long gpPatientsCount;
    private long medicalRecordsCount;
    private BigDecimal totalEarnings;
}
