package bg.nbu.medialrecordapp.data.dto.Doctor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class DoctorInsertRequestDTO {
    private String firstName;
    private String lastName;
    private String specialty;
    private boolean qualifiedForGeneralPractitioner;
}
