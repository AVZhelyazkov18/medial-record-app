package bg.nbu.medialrecordapp.data.dto.Patient;

import bg.nbu.medialrecordapp.enums.HealthInsuranceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientHomeResponseDTO {
    private String firstName;
    private String lastName;
    private String gpFirstName;
    private String gpLastName;
    private HealthInsuranceStatus status;
}
