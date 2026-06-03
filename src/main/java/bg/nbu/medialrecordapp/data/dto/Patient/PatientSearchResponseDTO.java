package bg.nbu.medialrecordapp.data.dto.Patient;

import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.enums.HealthInsuranceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientSearchResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private HealthInsuranceStatus healthInsuranceStatus;
    private Doctor generalPractitionerName;
}